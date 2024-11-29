package com.romoreno.compraplus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase que hereda el contexto de la aplicación
 * Necesaria para poder usar Dagger Hilt (Inyección de dependenciaS)
 *
 * @author: Roberto Moreno
 */
@HiltAndroidApp
class CompraPlusApp : Application()
