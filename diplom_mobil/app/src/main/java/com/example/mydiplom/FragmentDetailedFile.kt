package com.example.mydiplom

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.File
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.databinding.FragmentAddFileBinding
import com.example.mydiplom.databinding.FragmentDetailedFileBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentDetailedFile : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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

    private var binding: FragmentDetailedFileBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var imageView: ImageView
    private val FILE_PICK_CODE = 1000
    private  var file : java.io.File? = null
    private  var fileType:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedFileBinding.inflate(inflater, container, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var fileId = viewModel.fileId.value ?: 1
        val call: Call<File> = service.getFileById(fileId)


        call.enqueue(object : Callback<File> {
            override fun onResponse(call: Call<File>, response: Response<File>) {
                if (response.isSuccessful) {
                    var fileData = response.body()
                    fileData?.let {
                        binding!!.detailedFileName.setText(it.name.toString())
                        binding!!.detailedFileComment.setText(it.comment.toString())
                        binding!!.detailedFileDate.setText(formatDate(it.date.toString()))
                        val imageUrl = "http://10.0.2.2:3000/files/" + it.link
                        if (it.mime_type.split("/")[0] != "image") {
                            Picasso.get()
                                .load(imageUrl)
                                .into(binding!!.detailedImageFileFile)
                            imageView.setImageResource(R.drawable.icondocument)
                        } else {
                            Picasso.get()
                                .load(imageUrl)
                                .into(binding!!.detailedImageFileFile)
                        }
//                        Log.d("RetrofitClient","java.io.File(imageUrl.path) " + java.io.File(imageUrl))
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<File>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        binding!!.button.setOnClickListener{
            //почему- не работает очищение фото!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            binding!!.detailedImageFileFile.setImageURI(null)
            file = null
            fileType = null
            binding!!.detailedFileImageFileLabel.setText("Выберите файл:")
            binding!!.button.visibility = View.INVISIBLE
        }

        imageView = binding!!.detailedImageFileFile
        val pickFileButton  = binding!!.bthAddImageFileDetailedFile
        pickFileButton.setOnClickListener {
            openFileChooser()
        }


        binding!!.bthAddDetailedFileToBD.setOnClickListener {

            if( file != null && !binding!!.detailedFileName.text.isNullOrEmpty() && !binding!!.detailedFileDate.text.isNullOrEmpty()) {

                val name = binding!!.detailedFileName.text.toString()
                val comment = binding!!.detailedFileComment.text.toString()
                val date = binding!!.detailedFileDate.text.toString()


                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val filePart = MultipartBody.Part.createFormData("file", file?.name, requestFile)

                val fileTypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), fileType)
                val dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), date)
                val nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
                val commentRequestBody = RequestBody.create(MediaType.parse("text/plain"), comment)

                val call: Call<Void> = service.updateFile(
                    fileId,
                    filePart,
                    fileTypeRequestBody,
                    dateRequestBody,
                    nameRequestBody,
                    commentRequestBody
                )
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
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

        val context = activity ?: return binding!!.root
        binding!!.bthAddDateDetailedFile.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }

        binding!!.bthDeleteDetailedFile.setOnClickListener{
            val call: Call<Void> = service.deleteFile(fileId)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("RetrofitClient", "Receive user from server problem " + t)
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            })
            findNavController().navigate(R.id.fragmentListFiles)
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
            file = java.io.File(fileUri.path)

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

            binding!!.detailedFileImageFileLabel.setText("Выбран файл")
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

        binding!!.detailedFileDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


}