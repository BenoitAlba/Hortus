package org.alba.hortus.utils

import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.AudioToolbox.kSystemSoundID_Vibrate

actual fun vibratePhone() {
    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
}