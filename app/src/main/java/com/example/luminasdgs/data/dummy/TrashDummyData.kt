package com.example.luminasdgs.data.dummy

import com.example.luminasdgs.R
import com.example.luminasdgs.data.model.TrashItem
import com.example.luminasdgs.utils.Constants

object TrashDummyData {
    val items = listOf(
        TrashItem("Botol plastik", Constants.BIN_KUNING, "Plastik", R.drawable.botolplastik),
        TrashItem("Kantong plastik", Constants.BIN_KUNING, "Plastik", R.drawable.kresek),
        TrashItem("Gelas plastik", Constants.BIN_KUNING, "Plastik", R.drawable.gelasplastik),
        TrashItem("Baterai", Constants.BIN_MERAH, "B3", R.drawable.baterai),
        TrashItem("Lampu rusak", Constants.BIN_MERAH, "B3", R.drawable.lampurusak),
        TrashItem("Kaleng cat", Constants.BIN_MERAH, "B3", R.drawable.kalengcat),
        TrashItem("Kulit pisang", Constants.BIN_HIJAU, "Organik", R.drawable.kulitpisang),
        TrashItem("Sisa sayur", Constants.BIN_HIJAU, "Organik", R.drawable.sayur),
        TrashItem("Daun kering", Constants.BIN_HIJAU, "Organik", R.drawable.daunkering),
        TrashItem("Koran bekas", Constants.BIN_BIRU, "Kertas", R.drawable.koran),
        TrashItem("Kardus", Constants.BIN_BIRU, "Kertas", R.drawable.kardus),
        TrashItem("Buku rusak", Constants.BIN_BIRU, "Kertas", R.drawable.buku),
        TrashItem("Popok sekali pakai", Constants.BIN_ABU, "Residu", R.drawable.popok),
        TrashItem("Puntung rokok", Constants.BIN_ABU, "Residu", R.drawable.puntungrokok),
        TrashItem("Masker sekali pakai", Constants.BIN_ABU, "Residu", R.drawable.masker)
    )
}
