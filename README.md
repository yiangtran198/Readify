figma link: https://www.figma.com/design/PbSsCJbh6pfmRdAxOfBpQ1/LTUD-Readify?node-id=0-1&p=f&t=MJUbfyn4SCof73h0-0

# Hướng dẫn tích hợp Firebase và lấy dữ liệu cho ứng dụng đọc truyện trên Android

Tài liệu này dành cho các thành viên nhóm sử dụng Firebase đã được thiết lập sẵn để:

* Đăng nhập người dùng
* Lưu trữ trạng thái đang đọc
* Lưu danh sách truyện yêu thích
* Lấy danh sách truyện và nội dung chapter

---

### a. Đăng ký người dùng bằng Email & Mật khẩu

```kotlin
FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val uid = task.result?.user?.uid
            // Lưu thêm thông tin user nếu cần
        }
    }
```

### b. Đăng nhập người dùng

```kotlin
FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            // Dùng uid để lưu truyện, trạng thái đọc, v.v.
        }
    }
```

### c. Lấy UID hiện tại sau khi đã đăng nhập

```kotlin
val uid = FirebaseAuth.getInstance().currentUser?.uid
```

> 🔐 UID này sẽ dùng làm `userId` trong collection `users`

---

## 3. Cấu trúc dữ liệu Firebase Firestore

### Collection: `comics`

```plaintext
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

### Collection: `users`

```plaintext
users (collection)
 └── userId (trùng với Firebase Auth UID)
     ├── favorites: [comicId1, comicId2, ...]
     └── reading_status:
         └── comicId:
             ├── chapter_id: string
             └── page_index: number
```

---

## 4. Cách lấy dữ liệu trong Android (Kotlin)

### a. Lấy danh sách truyện

```kotlin
val db = FirebaseFirestore.getInstance()
db.collection("comics")
    .get()
    .addOnSuccessListener { result ->
        for (doc in result) {
            val title = doc.getString("title")
            // ...
        }
    }
```

### b. Lấy danh sách chapter

```kotlin
db.collection("comics")
    .document(comicId)
    .collection("chapters")
    .get()
    .addOnSuccessListener { result ->
        for (doc in result) {
            val title = doc.getString("title")
        }
    }
```

### c. Lấy nội dung một chapter

```kotlin
db.collection("comics")
    .document(comicId)
    .collection("chapters")
    .document(chapterId)
    .get()
    .addOnSuccessListener { doc ->
        val pages = doc.get("pages") as List<*>
    }
```

---

## 5. Lưu truyện yêu thích

```kotlin
db.collection("users")
    .document(uid)
    .update("favorites", FieldValue.arrayUnion(comicId))
```

## 6. Cập nhật trạng thái đang đọc

```kotlin
val readingStatus = hashMapOf(
    comicId to mapOf(
        "chapter_id" to "chapter2",
        "page_index" to 3
    )
)
db.collection("users")
    .document(uid)
    .set(mapOf("reading_status" to readingStatus), SetOptions.merge())
```

## 7. Lấy danh sách truyện yêu thích và trạng thái đọc

```kotlin
db.collection("users")
    .document(uid)
    .get()
    .addOnSuccessListener { doc ->
        val favorites = doc.get("favorites") as? List<*>
        val status = doc.get("reading_status") as? Map<*, *>
    }
```

---

## 8. Lưu ý chung

* Luôn đảm bảo người dùng đã login (FirebaseAuth)
* Để tránh ghi đè dữ liệu, luôn dùng `SetOptions.merge()`
* Có thể test dữ liệu trên Firestore bằng Firebase Console (tab Firestore Database)

---

Bạn nào muốn test nhanh có thể dùng tài khoản demo 
id:   `vYeqnac5MwhpbXi9ePxAd7lS5Ic2`
mail: `user01@gmail.com` 
pass: `123123`

