# Tugas Besar 1 Aljabar Linear dan Geometri 2023

## Anggota Kelompok
1. Shafiq Irvansyah (13522003)
2. Dewantoro Triatmojo (13522011)
3. Muhammad Althariq Fairuz (13522027)

## Deskripsi Singkat

Di dalam Tugas Besar 1 ini, Kami diminta membuat satu atau lebih library aljabar 
linier dalam Bahasa Java. Library tersebut berisi fungsi-fungsi seperti eliminasi Gauss, 
eliminasi Gauss-Jordan, menentukan balikan matriks, menghitung determinan, kaidah 
Cramer (kaidah Cramer khusus untuk SPL dengan n peubah dan n persamaan). 
Selanjutnya, gunakan library tersebut di dalam program Java untuk menyelesaikan 
berbagai persoalan yang dimodelkan dalam bentuk SPL, menyelesaikan persoalan 
interpolasi, dan persoalan regresi. 

## Struktur Folder Program

```bash
Algeo01-22003
├───bin
│   ├───errors
│   ├───lib
│   ├───matrix
│   └───program
├───doc
├───src
│   ├───errors
│   ├───lib
│   ├───matrix
│   └───program
└───test
    ├───input
    └───output
```

## Cara Menjalankan Program

Pertama, clone repository ini terlebih dahulu.

```shell
git clone https://github.com/dewodt/Algeo01-22003.git
```

Pastikan anda berada pada root directory projek ini yaitu `../foo/Algeo01-22003/`. Kemudian run command berikut untuk mengcompile source code yang berada pada folder `/src`.

```shell
javac --source-path src -d bin src/Main.java
```

Kemudian run command berikut untuk menjalankan file class berada pada folder `/bin`.

```shell
java -cp bin Main
```

## Fitur

Program memiliki beberapa fitur yang berhubungan dengan operasi matriks dan aplikasinya seperti:
1. Menyelesaikan sistem persamaan linear dengan metode Gauss, Gauss Jordan, Cramer, dan Matriks Balikan.
2. Mendapatkan determinan suatu matriks dengan metode operasi baris elementer dan ekspansi kofaktor.
3. Mendapatkan balikan matriks dengan metode operasi baris elementer dan adjoin & determinan.
4. Melakukan interpolasi polinom
5. Melakukan interpolasi bicubic spline
6. Melakukan regresi linear berganda

## Links

Berikut beberapa link penting yang berkaitan dengan tugas ini.

- [Repository Project](https://github.com/dewodt/Algeo01-22003) 
- Laporan Tugas
