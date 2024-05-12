<template>
    <div class="container" >

    
            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля название, дата и файл должны быть обязательно заполнены</p>

            <div  class="labelm" >
                <div class="row justify-content-md-center ">
                    <h5 class="text-center fs-4 col-md-5 labelm" >Добавление нового файла</h5>
                </div>
                
                <div class="row justify-content-md-center ">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
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
                        <div class="mb-3 labelm">
                            <label for="inputName" class="form-label">Файл</label>
                            <input type="file" class="form-control" id="inputName" @change="handleFileUpload" > 
                        </div>
                        <button type="button" class="btn btn-outline-success " @click="addFile()">Добавить запись с файлом</button>
                        <div class="alert alert-success" role="alert" v-if="showAlert">
                            Успешно!
                        </div>
                    </div>
                </div>
            </div>

            
    </div>


</template>

<script setup>

import { ref,computed  } from 'vue';
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useStore } from 'vuex';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);


    const name= ref(null)
    const comment= ref(null)
    const date= ref(null)
    const file= ref(null)


    // const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);


    function handleFileUpload(event) {
        file.value = event.target.files[0];
    }



    async function addFile() {
        if (name.value && date.value && file.value) {
            isFormValid.value = true;
            try {
                const formData = new FormData();
                formData.append('file', file.value);
                formData.append('name', name.value);
                formData.append('date', date.value);
                formData.append('comment', comment.value);

                await http.post('/addFileC/' + currentUser.value.id, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                }, 1000);
            } catch (error) {
                console.error(error);
            }
        } else {isFormValid.value = false;}
    }

    
  

</script>


<style>

.labelm {
    margin-top: 20px;
}

.bthM{
    margin: 20px;
}


</style>