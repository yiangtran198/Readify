figma link: https://www.figma.com/design/PbSsCJbh6pfmRdAxOfBpQ1/LTUD-Readify?node-id=0-1&p=f&t=MJUbfyn4SCof73h0-0

## 🧪 Tài khoản Firebase demo

| Mục       | Giá trị                             |
|-----------|--------------------------------------|
| UID       | `vYeqnac5MwhpbXi9ePxAd7lS5Ic2`       |
| Email     | `user01@gmail.com`                  |
| Mật khẩu  | `123123`                            |
| Tên       | `Nguyễn Văn A`                      |
| Avatar    | `https://i.pravatar.cc/150?img=10`  |
| Birthday  | `2000-01-01`                        |

# 📚 Hướng dẫn tích hợp Firebase cho ứng dụng đọc truyện (Java)

Tài liệu này hướng dẫn sử dụng Firebase với Java để:

- Đăng nhập & đăng ký người dùng bằng email
- Lưu danh sách truyện, chapter, trạng thái đang đọc, và yêu thích
- Truy xuất dữ liệu từ Firestore
- Chỉnh sửa hồ sơ người dùng

---

## 1. Đăng ký người dùng

```java
FirebaseAuth.getInstance()
    .createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();

                // Lưu tên người dùng vào Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", "Tên người dùng"); // lấy từ EditText
                userData.put("created_at", FieldValue.serverTimestamp());

                db.collection("users")
                    .document(uid)
                    .set(userData, SetOptions.merge());
            }
        }
    });

```

## 2. Đăng nhập người dùng

```java
FirebaseAuth.getInstance()
    .signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    });
```

## 2. Lấy lại mật khẩu

```java
FirebaseAuth.getInstance()
    .sendPasswordResetEmail(email)
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Log.d("ResetPassword", "Email đặt lại mật khẩu đã được gửi.");
            } else {
                Log.e("ResetPassword", "Không thể gửi email: " + task.getException().getMessage());
            }
        }
    });
```

## 3. Lấy UID hiện tại

```java
String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
```

## 4. Lấy danh sách truyện

```java
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("comics")
    .get()
    .addOnSuccessListener(query -> {
        for (DocumentSnapshot doc : query.getDocuments()) {
            String title = doc.getString("title");
        }
    });
```

## 5. Lấy danh sách chapter của một truyện

```java
db.collection("comics")
    .document(comicId)
    .collection("chapters")
    .get()
    .addOnSuccessListener(query -> {
        for (DocumentSnapshot doc : query.getDocuments()) {
            String title = doc.getString("title");
        }
    });
```

## 6. Lấy nội dung một chapter

```java
db.collection("comics")
    .document(comicId)
    .collection("chapters")
    .document(chapterId)
    .get()
    .addOnSuccessListener(doc -> {
        List<String> pages = (List<String>) doc.get("pages");
    });
```

## 7. Thêm truyện vào danh sách yêu thích

```java
db.collection("users")
    .document(uid)
    .update("favorites", FieldValue.arrayUnion(comicId));
```

## 8. Cập nhật trạng thái đang đọc

```java
Map<String, Object> chapterInfo = new HashMap<>();
chapterInfo.put("chapter_id", "chapter2");
chapterInfo.put("page_index", 3);

Map<String, Object> readingStatus = new HashMap<>();
readingStatus.put(comicId, chapterInfo);

db.collection("users")
    .document(uid)
    .set(Collections.singletonMap("reading_status", readingStatus), SetOptions.merge());
```

## 9. Lấy danh sách yêu thích và trạng thái đang đọc

```java
db.collection("users")
    .document(uid)
    .get()
    .addOnSuccessListener(doc -> {
        List<String> favorites = (List<String>) doc.get("favorites");
        Map<String, Object> status = (Map<String, Object>) doc.get("reading_status");
    });
```

## 10. Cập nhật hồ sơ người dùng (Profile)

```java
Map<String, Object> profileUpdate = new HashMap<>();
profileUpdate.put("name", "Nguyễn Văn A");
profileUpdate.put("birthday", "2000-01-01");
profileUpdate.put("avatar", "https://i.pravatar.cc/150?img=10");

db.collection("users")
    .document(uid)
    .set(profileUpdate, SetOptions.merge());
```

---

## 🔧 Cấu trúc Firestore mẫu

### 📁 `comics` Collection

```
comics
 └── comicId
     ├── title
     ├── author
     ├── description
     ├── categories: [string]
     ├── status: string
     ├── created_at: Timestamp
     └── chapters (subcollection)
         └── chapterId
             ├── title
             ├── chapter_number
             ├── pages: [string]
             └── created_at
```

### 📁 `users` Collection

```
users
 └── userId (UID từ FirebaseAuth)
     ├── name: string
     ├── birthday: string (YYYY-MM-DD)
     ├── avatar: string (URL)
     ├── favorites: [comicId]
     ├── reading_status:
         └── comicId:
             ├── chapter_id: string
             └── page_index: number
     └── created_at: Timestamp
```

---

## ✅ Ghi chú

- Luôn dùng `SetOptions.merge()` để tránh ghi đè toàn bộ document khi cập nhật một phần.
- Với người dùng mới, bạn nên thiết lập các trường `name`, `avatar`, `birthday` sau khi tạo tài khoản.
- Firestore không giới hạn số lượng trường trong document, nhưng nên tối ưu cho mobile.

---


