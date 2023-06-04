# Storage
Storage uygulaması, firebase altyapısını kullanarak oluşturulan ürünleri ve ürünlerin stok adedlerini çevrimiçi ortama eş zamanlı olarak kaydeder. Uygulama silinse bile kullanıcı girişi ile stok kontrolu yapılabilir.

## Login Page
![login](https://github.com/Emreren11/Storage/assets/113580478/44e795f2-a79b-451c-b1f4-3a3d0080865b)

***Sign In*** : Firebase ile kontrol sağlar ve giriş yapar.

***Sign Up*** : Verilen bilgiler doğrultusunda Firebase içerisine kayıt olunur. Kayıt olma işlemi olduğunda kişinin vermiş olduğu email bilgisi ile bir "collection" oluşturulur.


## Home Page
![home](https://github.com/Emreren11/Storage/assets/113580478/4e41938c-6406-4023-924a-99b55125dbbd)
![home_addProduct](https://github.com/Emreren11/Storage/assets/113580478/c1450a20-0724-4509-92c9-d50825a18ee9)
![home_addStock](https://github.com/Emreren11/Storage/assets/113580478/23f82f69-8517-45b9-a9f7-ba3634951c56)
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
![storage](https://github.com/Emreren11/Storage/assets/113580478/da2586d0-e91e-4f0b-9dac-03084013c952)
- BottomNavigtaionView içerisindeki ***Storage*** tuşu ile açılır.
- Spinner ile ürün seçimi yapıldığında firestore içerisinden data çekilir.
- Çekilen data recyclerView içerisine aktarılır.
- ***Total Stock*** : Seçilen üründen toplam kaç adet olduğu görüntülenir.
- ***Total Price*** : Seçilen ürünün tüm renklerinin toplam cirosu görüntülenir.
