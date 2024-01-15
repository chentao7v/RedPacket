package me.chentao.redpacket.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import me.chentao.redpacket.databinding.ToastBinding

/**
 * create by chentao on 2024-01-15.
 */


fun showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
  if (isUiThread()) {
    show(msg, duration)
  } else {
    runOnUiThread { show(msg, duration) }
  }
}

private fun show(msg: CharSequence, duration: Int) {
  val binding = ToastBinding.inflate(LayoutInflater.from(app))
  binding.toast.text = msg

  val toast = Toast(app)
  toast.duration = duration
  toast.setGravity(Gravity.CENTER, 0, 0)
  toast.view = binding.root
  toast.show()
}