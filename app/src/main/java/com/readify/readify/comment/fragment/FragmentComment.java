package com.readify.readify.comment.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.readify.readify.R;
import com.readify.readify.Model.Review;
import com.readify.readify.comment.adapter.ReviewAdapter;
import com.readify.readify.home.adapter.BookAdapter;
import com.readify.readify.home.adapter.PopularBookAdapter;
import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.BookViewModel;
import com.readify.readify.home.model.ReviewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FragmentComment extends Fragment {

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private ImageView[] ratingStars;
    private EditText editTextReview;
    private ImageButton btnSendReview;
    private int currentRating = 0; // Để lưu số sao người dùng đã chọn
    private TextView txtRatingScore;

    // Firebase Auth
    private FirebaseAuth mAuth;
    private Book book;
    private BookViewModel bookViewModel;

    public FragmentComment() {
        // Constructor rỗng là bắt buộc cho Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout của bạn
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        initializeUI(view);
        setupReviewList();
        setupStarRating();
        setupSendButton();
        bookViewModel.fetchBooks();

    }

    private void initializeUI(View view) {
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editTextReview = view.findViewById(R.id.editTextReview);
        btnSendReview = view.findViewById(R.id.btnSendReview);
        txtRatingScore = view.findViewById(R.id.txtRatingScore);

        ratingStars = new ImageView[5];
        ratingStars[0] = view.findViewById(R.id.star1);
        ratingStars[1] = view.findViewById(R.id.star2);
        ratingStars[2] = view.findViewById(R.id.star3);
        ratingStars[3] = view.findViewById(R.id.star4);
        ratingStars[4] = view.findViewById(R.id.star5);
    }

    private void setupReviewList() {
        reviewList = new ArrayList<>();

//        for (Review comment : book.comments) {
//            Review review = new Review(comment.getReviewerName(), comment.getContent(), comment.getRating()); // Giả sử Review có constructor nhận Comment
//            reviewList.add(review);
//        }

        bookViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                for (Book b : books) {
                    if (Objects.equals(b.id, book.id)) {
                        if (b.comments != null && !b.comments.isEmpty()) {
                            for (Review comment : b.comments) {
                                Review review = new Review(
                                        comment.getReviewerName(),
                                        comment.getContent(),
                                        comment.getRating()
                                );
                                reviewList.add(review);
                            }
                        }
                    }
                }
                reviewAdapter.notifyDataSetChanged();
            }
        });

        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }


    private void setupStarRating() {
        for (int i = 0; i < ratingStars.length; i++) {
            final int starPosition = i;
            ratingStars[i].setOnClickListener(v -> updateStarRating(starPosition));
        }
    }

    private void updateStarRating(int position) {
        currentRating = position + 1; // Cập nhật số sao hiện tại (1-5)

        for (int i = 0; i < ratingStars.length; i++) {
            ratingStars[i].setImageResource(i <= position ?
                    R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        }
    }

    private void setupSendButton() {
        btnSendReview.setOnClickListener(v -> {
            String reviewContent = editTextReview.getText().toString().trim();

            // Kiểm tra người dùng đã đăng nhập chưa
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để gửi nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem người dùng đã nhập comment và đánh giá sao chưa
            if (reviewContent.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentRating == 0) {
                Toast.makeText(getContext(), "Vui lòng đánh giá sao", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thêm review mới
            addNewReview(currentUser, reviewContent, currentRating);
        });
    }

    private void addCommentToBook(String bookId, String name, String content, int rating) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo comment mới
        Map<String, Object> newComment = new HashMap<>();
        newComment.put("name", name);
        newComment.put("content", content);
        newComment.put("rating", rating);

        DocumentReference docRef = db.collection("comics").document(bookId);

        // Thêm comment vào mảng 'comments', tạo document nếu chưa có
        docRef.update("comments", FieldValue.arrayUnion(newComment))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Comment added successfully.");
                })
                .addOnFailureListener(e -> {
                    // Nếu document chưa tồn tại thì tạo mới và thêm comments
                    if (e instanceof FirebaseFirestoreException &&
                            ((FirebaseFirestoreException) e).getCode() == FirebaseFirestoreException.Code.NOT_FOUND) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("comments", Arrays.asList(newComment));
                        docRef.set(data)
                                .addOnSuccessListener(unused -> Log.d("Firestore", "Document created and comment added."))
                                .addOnFailureListener(ex -> Log.e("Firestore", "Failed to create document", ex));
                    } else {
                        Log.e("Firestore", "Error adding comment", e);
                    }
                });
    }



    private void resetStarRating() {
        currentRating = 0;
        for (ImageView star : ratingStars) {
            star.setImageResource(R.drawable.ic_star_outline);
        }
    }

    private void addNewReview(FirebaseUser user, String content, int rating) {
        // Lấy ngày hiện tại theo định dạng "dd thg MM yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'thg' MM yyyy", new Locale("vi", "VN"));
        String currentDate = sdf.format(new Date());

        // Lấy tên người dùng từ Firebase Auth
        String userName = getUserName(user);

        // Tạo đối tượng Review mới
        Review newReview = new Review(userName, content, rating);

        // Thêm nhận xét mới vào đầu danh sách
        reviewList.add(0, newReview);

        // Thông báo cho adapter biết có dữ liệu mới
        reviewAdapter.notifyItemInserted(0);

        // Cuộn lên đầu để hiển thị nhận xét mới
        reviewRecyclerView.smoothScrollToPosition(0);

        // Xóa nội dung đã nhập và reset sao
        editTextReview.setText("");
        resetStarRating();

        // Cập nhật điểm trung bình
        updateAverageRating();

        addCommentToBook(book.id,userName,content, rating);
        Log.d("--------------", "addNewReview: "+book.id);

        // Hiển thị thông báo
        Toast.makeText(getContext(), "Đã gửi nhận xét thành công", Toast.LENGTH_SHORT).show();
    }

    // Hàm lấy tên người dùng từ Firebase User
    private String getUserName(FirebaseUser user) {
        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            return user.getDisplayName();
        } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            // Nếu không có DisplayName, dùng phần trước @ của email
            return user.getEmail().split("@")[0];
        } else {
            // Trường hợp không có cả email và displayName
            return "Người dùng";
        }
    }

    // Cập nhật điểm trung bình hiển thị
    private void updateAverageRating() {
        if (reviewList.isEmpty()) {
            return;
        }

        float totalRating = 0;
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }

        float averageRating = totalRating / reviewList.size();

        // Cập nhật điểm trung bình hiển thị
        txtRatingScore.setText(String.format(Locale.getDefault(), "%.1f", averageRating));
    }
}