package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class PerfilMascotaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
}
}