<template>
    <div class="container" >

        <div v-if="displayContent">

            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля название и дата должны быть обязательно заполнены</p>

            <div  class=""  >
                <div class="row justify-content-md-center " style="margin-top: 20px">
                    <h5 class="text-center fs-4 col-md-5 labelm" >Редактирование записи с файлом</h5>
                </div>
                
                <div class="row justify-content-center"  style="margin-top: 40px">
                    <div  class="col-md-5 ">
                        <div class="mb-3 ">
                            <label for="inputName" class="form-label">Название</label>
                            <input type="text" class="form-control" id="inputName"  v-model="name"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputName" class="form-label">Комментарий</label>
                            <input type="text" class="form-control" id="inputName"  v-model="comment"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputDateBirth" class="form-label">Дата </label>
                            <input type="datetime-local" class="form-control" id="inputDateBirth"  v-model="date"> 
                        </div>

                    </div>
                    <div  class="col-md-5 " style="margin-left: 40px; margin-top: 8px">
                        <div>
                            <label for="file" class="form-label">Загруженный файл</label>
                            <div class="mb-3 ">
                                
                                <img :src="getLink(imgFile, mimeType)" width="100" v-if="['image/png', 'image/jpg', 'image/jpeg'].includes(mimeType) ">
                                <svg width="100px" height="100px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" v-else>
                                    <rect width="24" height="24" fill="white"/>
                                    <path d="M13 3L16 6L19 9M13 3L5 3L5 21L19 21L19 9M13 3L13 9L19 9" stroke="#000000" stroke-linejoin="round"/>
                                </svg>

                                <div>
                                    <a :href="getLink(imgFile, mimeType)" download="file">Скачать файл</a>
                                </div>
                            </div>
                            
                        </div>
                        <div>
                            <label for="inputName" class="form-label">Выберите новый файл</label>
                            <input type="file" class="form-control" id="inputName" @change="handleFileUpload" > 
                        </div>
                    </div>
                </div>

                <div class="row justify-content-md-center " style="margin-top: 50px">
                    
                    <div > 
                            <button type="button" class="btn btn-outline-success bthM" @click="updateFile()">Обновить запись с файлом</button>
                            <button type="button" class="btn btn-outline-danger bthM" @click="deleteFile()">Удалить запись</button>
                    </div>
                

                    <div class="alert alert-success labelm " role="alert" v-if="showAlert">
                        Успешно!
                    </div>

                </div>
                
            </div>

        </div>
        <div v-else>
            {{ content.message }}
        </div>
            
    </div>


</template>

<script setup>

import { ref, onMounted  } from 'vue';
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import UserService from '../../services/user.service';

    const moment = require('moment');
    const router = useRouter();
    const store = useStore();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))

    const name= ref(data.value.name)
    const comment= ref(data.value.comment)
    const date= ref(moment.utc(data.value.date).format('YYYY-MM-DD HH:mm'))
    const file= ref(null)

    const imgFile = ref(null)
    const mimeType = ref(null)

    const displayContent= ref(false)
    const content = ref('')
    // const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);


    function handleFileUpload(event) {
        file.value = event.target.files[0];
    }



    async function updateFile() {
        if (name.value && date.value && file.value ) {
            isFormValid.value = true;
            try {
                if(comment.value == null){
                    comment.value =""
                }
                const formData = new FormData();
                formData.append('file', file.value);
                formData.append('name', name.value);
                formData.append('date', date.value);
                formData.append('comment', comment.value);

                await http.post('/updateFileC/' + data.value.id, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                    router.push({ path: '/listFile'});
                }, 1000);
            } catch (error) {
                // console.error(error);
                if(error.response.status == 401 || error.response.status == 403){
                await store.dispatch('auth/logout')
                router.push('/login')
                }
            }
        } else {
            if (name.value && date.value && imgFile.value ) {
                isFormValid.value = true;
                try {
                    if(comment.value == null){
                        comment.value =""
                    }
                    var a = {
                        userId: data.value.userId,
                        name: name.value,
                        date: date.value,
                        comment: comment.value
                    };
                    await http.post('/updateWithoutFileC/' + data.value.id, a);
                    showAlert.value = true
                    setTimeout(() => {
                        showAlert.value = false;
                        router.push({ path: '/listFile'});
                    }, 1000);
                } catch (error) {
                    // console.error(error);
                    if(error.response.status == 401 || error.response.status == 403){
                    await store.dispatch('auth/logout')
                    router.push('/login')
                    }
                }
            }
            else{ isFormValid.value = false;}
        }
    }


    async function deleteFile() {
            try {
                await http.post('/deleteFileC/' + data.value.id);
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                    router.push({ path: '/listFile'});
                }, 1000);
            } catch (error) {
                // console.error(error);
                if(error.response.status == 401 || error.response.status == 403){
                await store.dispatch('auth/logout')
                router.push('/login')
                }
            }
    }
    
    const getFile = async (id) => {
      try {
          const response = await http.get('/fileC/' + id);
          imgFile.value = response.data.file
          mimeType.value = response.data.mime_type

      } catch (error) {
          // console.error(error);
            if(error.response.status == 401 || error.response.status == 403){
            await store.dispatch('auth/logout')
            router.push('/login')
            }
      }
  }

    function getLink(base64, mime_type){
        var byteCharacters = atob(base64);
        var byteNumbers = new Array(byteCharacters.length);
        for (var i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        var file = new Blob([byteArray], { type: mime_type });
        var fileURL = URL.createObjectURL(file);
        return fileURL;
    }


    const fetchUserBoard = async () => {
        try {
        await UserService.getUserBoard()
        displayContent.value = true
        } catch (e) {
        // displayContent.value = false
        content.value = (e.response && e.response.data) || e.message || e.toString()
        }
    }

  onMounted(async () => {
    fetchUserBoard()
    await getFile(data.value.id);
});

</script>


<style>

.labelm {
    margin-top: 20px;
}

.bthM{
    margin: 20px;
    width: 300px;
}




</style>