# Storage
Storage uygulaması, firebase altyapısını kullanarak oluşturulan ürünleri ve ürünlerin stok adedlerini çevrimiçi ortama eş zamanlı olarak kaydeder. Uygulama silinse bile kullanıcı girişi ile stok kontrolu yapılabilir.

## Login Page
![loginEN](https://github.com/Emreren11/Storage/assets/113580478/41815329-129d-46f0-b3cc-9530d40f37cd) ---->
![loginTR](https://github.com/Emreren11/Storage/assets/113580478/bb7ec402-8e24-4053-8ef3-f8d040eed756)

***Sign In*** : Firebase ile kontrol sağlar ve giriş yapar.

***Sign Up*** : Verilen bilgiler doğrultusunda Firebase içerisine kayıt olunur. Kayıt olma işlemi olduğunda kişinin vermiş olduğu email bilgisi ile bir "collection" oluşturulur.

***English - Türkçe*** : Seçilen dil seçeneğine göre arayüzü yeniler ve firestore'a dil bilgisini işler. Her giriş yapıldığında değiştirilebilir.


## Home Page
![home](https://github.com/Emreren11/Storage/assets/113580478/479d0e9d-6581-48ac-add5-1c9a43bbfb76) ---->
![home_addProduct](https://github.com/Emreren11/Storage/assets/113580478/34f25a33-346d-4d84-8a1d-599a8c342917) ---->
![home_addStock](https://github.com/Emreren11/Storage/assets/113580478/eb6787c1-066a-4a1a-a11c-2a26a6b14dcf)

![homeTR](https://github.com/Emreren11/Storage/assets/113580478/83e6adbd-42d8-42df-b426-e075bf5edd7f) ---->
![homeTR_product](https://github.com/Emreren11/Storage/assets/113580478/056e2215-a170-4d91-a49d-33202fb389b2) ---->
![homeTR_stock](https://github.com/Emreren11/Storage/assets/113580478/60ca9cb5-8f9d-480a-bdb2-861473dd9ded)

- Activity açıldığında ilk gösterilen fragmandır.
- BottomNavigationView içerisinde olan ***Home*** tuşuyla da açılabilir.

### ***Product*** 
- Depolanan ürünlerin kaydedildiği kısımdır.

***Add / Update*** : **Login Page** sayfasında oluşturulan koleksiyonun içerisine **product** dökümanı oluşturur. Bu dökümana yeni koleksiyon ve **ürün ismi** ile döküman oluşturup ürün ismi, ürün fiyatı ve para birimi bilgisini kaydeder. Kaydetme işleminin ardından **Stock** kısmındaki spinner içerisine eklenir. Halihazırda ürün varsa girilen yeni bilgiler güncellenir.

### ***Stock***
- Spinner içerisin eklenmiş ürün varsa *Delete* ve *Add* butonları aktif olur.

***Add Butonu*** : **Login Page** sayfasında oluşturulan koleksiyonun içerisine **storage** dökümanı oluşturur. Bu dökümana yeni koleksiyon, ***ürün rengi*** ve ***ürün ismi*** ile döküman oluşturulup ürün ismi, ürün rengi, stok adedi bilgisini kaydeder. 

***Delete Butonu*** : *Add butonu* ile oluşturulan döküman içerisinde kontrol yapar. Kontrol sonucunda girilen bilgiler doğrultusunda azaltma işlemini yapar.

## Storage Page
![storageEN](https://github.com/Emreren11/Storage/assets/113580478/bcb2e49a-1710-43af-8425-394002579b77) ---->
![storageTR](https://github.com/Emreren11/Storage/assets/113580478/73a2ca29-a718-4295-babd-6c6599bd2e88)

- BottomNavigtaionView içerisindeki ***Storage*** tuşu ile açılır.
- Spinner ile ürün seçimi yapıldığında firestore içerisinden data çekilir.
- Çekilen data recyclerView içerisine aktarılır.
- ***Total Stock*** : Seçilen üründen toplam kaç adet olduğu görüntülenir.
- ***Total Price*** : Seçilen ürünün tüm renklerinin toplam cirosu görüntülenir.
