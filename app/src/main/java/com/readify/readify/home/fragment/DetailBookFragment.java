package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;

public class DetailBookFragment extends Fragment {
    private Book book;

    public DetailBookFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_book, container, false);

        // Nhận dữ liệu Book từ arguments
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        // Bind dữ liệu vào view
        ImageView imgBookCover = view.findViewById(R.id.imgBookCover);
        TextView txtBookTitle = view.findViewById(R.id.txtBookTitle);
        TextView txtBookAuthor = view.findViewById(R.id.txtBookAuthor);
        TextView txtBookInfo = view.findViewById(R.id.txtBookInfo);
        TextView txtBookDescription = view.findViewById(R.id.txtBookDescription);

        if (book != null) {
            txtBookTitle.setText(book.title);
            txtBookAuthor.setText("by " + book.author);
            // Giả lập info và description
            txtBookInfo.setText("Sách\nTiếng anh · 26 trang");
            txtBookDescription.setText("Mô tả sách: ...");
            // Load ảnh
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.bg_book_cover_rounded)
                    .error(R.drawable.bg_book_cover_rounded);
            Glide.with(requireContext())
                    .load(book.image)
                    .apply(requestOptions)
                    .into(imgBookCover);
        }

        // Xử lý click các nút
        Button btnRead = view.findViewById(R.id.btnRead);
        Button btnFollow = view.findViewById(R.id.btnFollow);
        Button btnFavorite = view.findViewById(R.id.btnFavorite);
        btnRead.setOnClickListener(v -> Toast.makeText(getContext(), "Đọc sách: " + book.title, Toast.LENGTH_SHORT).show());
        btnFollow.setOnClickListener(v -> Toast.makeText(getContext(), "Theo dõi: " + book.title, Toast.LENGTH_SHORT).show());
        btnFavorite.setOnClickListener(v -> Toast.makeText(getContext(), "Yêu thích: " + book.title, Toast.LENGTH_SHORT).show());

        return view;
    }
}