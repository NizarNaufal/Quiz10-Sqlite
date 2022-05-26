package com.kelompok4.rumusbalok

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.kelompok4.rumusbalok.databinding.ActivityCountVolumeBalokBinding
import com.kelompok4.rumusbalok.model.Tasks


class MainActivity : AppCompatActivity(), TextWatcher {
    lateinit var binding : ActivityCountVolumeBalokBinding
    var panjang: String? = null
    var lebar: String? = null
    var tinggi: String? = null
    private lateinit var dbHandler : DatabaseHelper
    var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountVolumeBalokBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDB()
        binding.edtPanjang.addTextChangedListener(this)
        binding.edtLebar.addTextChangedListener(this)
        binding.edtTinggi.addTextChangedListener(this)
        cekValidasiForm()
        binding.btnHitung.setOnClickListener {
            val nilaipanjang = panjang?.toDouble()
            val nilailebar = lebar?.toDouble()
            val nilaitinggi = tinggi?.toDouble()
            val volume = nilaipanjang!! * nilailebar!! * nilaitinggi!!
            binding.tvHasil.text = volume.toString()

            saveDataToDB(nilaipanjang, nilailebar, nilaitinggi,volume)
        }
        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(STATE_RESULT)
            binding.tvHasil.text = result
        }
    }
    private fun initDB(){
        dbHandler = DatabaseHelper(this)
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val tasks: Tasks = dbHandler.getTask(intent.getIntExtra("Id",0))
            binding.edtPanjang.setText(tasks.height)
            binding.edtLebar.setText(tasks.width)
            binding.edtTinggi.setText(tasks.tall)
            binding.tvHasil.text = tasks.completed
        }
    }
    private fun saveDataToDB(nilaipanjang: Double, nilailebar: Double, nilaitinggi: Double, volume:Double) {
        val tasks = Tasks()
        tasks.height = nilaipanjang.toString()
        tasks.width = nilailebar.toString()
        tasks.tall = nilaitinggi.toString()
        tasks.completed = volume.toString()
        dbHandler.addTask(tasks)
    }

    @SuppressLint("ResourceAsColor")
    private fun cekValidasiForm() {
        panjang = binding.edtPanjang.text.toString()
        lebar = binding.edtLebar.text.toString()
        tinggi = binding.edtTinggi.text.toString()
        if (!TextUtils.isEmpty(panjang) && !TextUtils.isEmpty(lebar) && !TextUtils.isEmpty(tinggi)) {
            binding.btnHitung.isEnabled = true
            binding.btnHitung.setTextColor(resources.getColor(R.color.colorPrimary))
            binding.btnHitung.setBackgroundResource(R.drawable.btn_round_effect)
        } else {
            binding.btnHitung.isEnabled = false
            binding.btnHitung.setTextColor(resources.getColor(R.color.colorAccentDisable))
            binding.btnHitung.setBackgroundResource(R.drawable.disable_btn_round_effect)
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        cekValidasiForm()
    }

    override fun afterTextChanged(editable: Editable) {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, binding.tvHasil.text.toString())
    }
    companion object {
        private const val STATE_RESULT = "state_result"
    }
}