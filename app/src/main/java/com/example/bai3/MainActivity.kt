package com.example.bai3

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var edtMSSV: EditText
    private lateinit var edtHoTen: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var calendarView: CalendarView
    private lateinit var btnToggleCalendar: Button
    private lateinit var spinnerWard: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var chkSport: CheckBox
    private lateinit var chkMovies: CheckBox
    private lateinit var chkMusic: CheckBox
    private lateinit var chkAgree: CheckBox
    private lateinit var btnSubmit: Button

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các view
        edtMSSV = findViewById(R.id.edtMSSV)
        edtHoTen = findViewById(R.id.edtHoTen)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        calendarView = findViewById(R.id.calendarView)
        btnToggleCalendar = findViewById(R.id.btnToggleCalendar)
        spinnerWard = findViewById(R.id.spinnerWard)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerCity = findViewById(R.id.spinnerCity)

        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerWard = findViewById(R.id.spinnerWard)

        // Khởi tạo AddressHelper để đọc dữ liệu từ JSON
        val addressHelper = AddressHelper(resources)

        // Lấy danh sách Tỉnh/Thành
        val provinces = addressHelper.getProvinces()
        setupSpinner(spinnerCity, provinces)

        // Sự kiện chọn Tỉnh/Thành
        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Lấy tên Tỉnh/Thành được chọn
                val selectedProvince = provinces[position]

                // Lấy danh sách Quận/Huyện của Tỉnh/Thành được chọn
                val districts = addressHelper.getDistricts(selectedProvince)

                // Cập nhật Spinner Quận/Huyện
                setupSpinner(spinnerDistrict, districts)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Sự kiện chọn Quận/Huyện
        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Lấy tên Tỉnh/Thành và Quận/Huyện được chọn
                val selectedProvince = spinnerCity.selectedItem.toString()
                val selectedDistrict = spinnerDistrict.selectedItem.toString()

                // Lấy danh sách Phường/Xã của Quận/Huyện được chọn
                val wards = addressHelper.getWards(selectedProvince, selectedDistrict)

                // Cập nhật Spinner Phường/Xã
                setupSpinner(spinnerWard, wards)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        chkSport = findViewById(R.id.chkSport)
        chkMovies = findViewById(R.id.chkMovies)
        chkMusic = findViewById(R.id.chkMusic)
        chkAgree = findViewById(R.id.chkAgree)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Hiển thị hoặc ẩn CalendarView
        btnToggleCalendar.setOnClickListener {
            if (calendarView.visibility == View.GONE) {
                calendarView.visibility = View.VISIBLE
            } else {
                calendarView.visibility = View.GONE
            }
        }

        // Lấy ngày được chọn từ CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }

        // Xử lý khi nhấn nút Submit
        btnSubmit.setOnClickListener {
            val mssv = edtMSSV.text.toString().trim()
            val hoTen = edtHoTen.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val genderId = radioGroupGender.checkedRadioButtonId

            // Kiểm tra thông tin đã điền đủ hay chưa
            if (mssv.isEmpty() || hoTen.isEmpty() || email.isEmpty() || phone.isEmpty() || genderId == -1 || !chkAgree.isChecked) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và đồng ý điều khoản!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Thông tin đã được gửi thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }




}
