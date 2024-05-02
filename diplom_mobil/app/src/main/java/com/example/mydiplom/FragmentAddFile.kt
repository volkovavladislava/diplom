package com.example.mydiplom

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mydiplom.databinding.FragmentAddFileBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FragmentAddFile : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private var binding: FragmentAddFileBinding? = null

    private lateinit var imageView: ImageView
    private val FILE_PICK_CODE = 1000
    private  var file :File? = null
    private  var fileType:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddFileBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val context = activity ?: return binding!!.root
        binding!!.bthAddDateAddFile.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }
        binding!!.addFileDate.setText(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))

        imageView = binding!!.addImageFileFile
        val pickFileButton  = binding!!.bthAddImageFileAddFile
        pickFileButton.setOnClickListener {
            openFileChooser()
        }


        binding!!.bthAddAddFileToBD.setOnClickListener {
            if( file != null && !binding!!.addFileName.text.isNullOrEmpty() && !binding!!.addFileDate.text.isNullOrEmpty()) {

                val name = binding!!.addFileName.text.toString()
                val comment = binding!!.addFileComment.text.toString()
                val date = binding!!.addFileDate.text.toString()


                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val filePart = MultipartBody.Part.createFormData("file", file?.name, requestFile)

                val fileTypeRequestBody =
                    RequestBody.create(MediaType.parse("text/plain"), fileType)
                val dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), date)
                val nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
                val commentRequestBody = RequestBody.create(MediaType.parse("text/plain"), comment)

                val call: Call<Void> = service.addFile(
                    1,
                    filePart,
                    fileTypeRequestBody,
                    dateRequestBody,
                    nameRequestBody,
                    commentRequestBody
                )

//            val call: Call<Void> = service.addFile(1, filePart, fileType,date,name,comment)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("RetrofitClient", "Receive user from server problem " + t)
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(context, "Сначала добавьте название, дату и файл", Toast.LENGTH_SHORT).show()
            }
        }

        binding!!.button.setOnClickListener{
            imageView.setImageURI(null)
            imageView.setImageResource(0)
            file = null
            fileType = null
            binding!!.addFileImageFileLabel.setText("Выберите файл:")
            binding!!.button.visibility = View.INVISIBLE
        }




        return binding!!.root
    }





    private fun openFileChooser() {
        val fileChooserIntent = Intent(Intent.ACTION_GET_CONTENT)
        fileChooserIntent.type = "*/*"

        // Создаем Intent для выбора файлов или изображений
        val chooserIntent = Intent.createChooser(fileChooserIntent, "Выберите файл или изображение")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(fileChooserIntent))

        startActivityForResult(chooserIntent, FILE_PICK_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_PICK_CODE) {
            val selectedFileUri = data?.data
            handleSelectedFile(selectedFileUri)
        }
    }
    private fun handleSelectedFile(fileUri: Uri?) {
        if (fileUri != null) {
            file = File(fileUri.path)

            val fileName = file!!.name
            fileType = getMimeType(fileUri).toString()
            val type = fileType!!.split("/")[1]
            val papka = fileType!!.split("/")[0]

            val inputStream = requireContext().contentResolver.openInputStream(fileUri)
            file = createTempFile(fileName, "."+type, requireContext().cacheDir)
            file!!.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            if (papka != "image") {
                imageView.setImageResource(R.drawable.icondocument)
            } else {
                imageView.setImageURI(fileUri)
            }
            binding!!.addFileImageFileLabel.setText("Выбран файл")
            binding!!.button.visibility = View.VISIBLE

        }
    }
    private fun getMimeType(uri: Uri): String? {
        val cr = requireActivity().contentResolver
        return cr.getType(uri)
    }






    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(context, this, hour, minute, true).show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        val formattedDate = String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d %02d:%02d",
            savedYear, savedMonth+1, savedDay, savedHour, savedMinute
        )

        binding!!.addFileDate.setText(formattedDate)
//        binding!!.addFileDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }



}