figma link: https://www.figma.com/design/PbSsCJbh6pfmRdAxOfBpQ1/LTUD-Readify?node-id=0-1&p=f&t=MJUbfyn4SCof73h0-0

# 📚 Hướng dẫn tích hợp Firebase cho ứng dụng đọc truyện (Java)

Tài liệu này hướng dẫn sử dụng Firebase với Java để:

- Đăng nhập & đăng ký người dùng bằng email
- Lưu danh sách truyện, chapter, trạng thái đang đọc, và yêu thích
- Truy xuất dữ liệu từ Firestore

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
                // Lưu thêm thông tin người dùng nếu cần
            }
        }
    });
```

## 2. Đăng nhập người dùng

```java
FirebaseAuth.getInstance()
    .signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Sử dụng UID để truy xuất dữ liệu cá nhân
            }
        }
    });
```

## 3. Lấy UID hiện tại

```java
String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
```

> 🔐 UID này sẽ là khóa chính trong collection `users`.

## 4. Lấy danh sách truyện

```java
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("comics")
    .get()
    .addOnSuccessListener(queryDocumentSnapshots -> {
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
            String title = doc.getString("title");
            // Xử lý dữ liệu
        }
    });
```

## 5. Lấy danh sách chapter của một truyện

```java
db.collection("comics")
    .document(comicId)
    .collection("chapters")
    .get()
    .addOnSuccessListener(queryDocumentSnapshots -> {
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
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
    .addOnSuccessListener(documentSnapshot -> {
        List<String> pages = (List<String>) documentSnapshot.get("pages");
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

Map<String, Object> updateData = new HashMap<>();
updateData.put("reading_status", readingStatus);

db.collection("users")
    .document(uid)
    .set(updateData, SetOptions.merge());
```

## 9. Lấy danh sách yêu thích và trạng thái đang đọc

```java
db.collection("users")
    .document(uid)
    .get()
    .addOnSuccessListener(documentSnapshot -> {
        List<String> favorites = (List<String>) documentSnapshot.get("favorites");
        Map<String, Object> status = (Map<String, Object>) documentSnapshot.get("reading_status");
    });
```

## 🔧 Cấu trúc Firestore mẫu

### 📁 `comics` Collection

```
comics (collection)
 └── comicId (document)
     ├── title
     ├── author
     ├── description
     ├── categories (array)
     ├── status
     ├── created_at
     └── chapters (subcollection)
         └── chapterId (document)
             ├── title
             ├── chapter_number
             ├── pages (array of string)
             └── created_at
```

### 📁 `users` Collection

```
users (collection)
 └── userId (UID từ FirebaseAuth)
     ├── favorites: [comicId1, comicId2, ...]
     └── reading_status:
         └── comicId:
             ├── chapter_id: string
             └── page_index: number
```

## ✅ Ghi chú

- Luôn đảm bảo người dùng đã đăng nhập trước khi truy vấn Firestore.
- Khi ghi dữ liệu mà không muốn ghi đè toàn bộ, luôn dùng `SetOptions.merge()`.
- Bạn có thể test Firestore trên Firebase Console tab **Database**.

## 🧪 Tài khoản Firebase demo

| Mục            | Giá trị                         |
|----------------|----------------------------------|
| UID            | `vYeqnac5MwhpbXi9ePxAd7lS5Ic2`   |
| Email          | `user01@gmail.com`              |
| Mật khẩu       | `123123`                        |

