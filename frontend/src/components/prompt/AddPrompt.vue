<template>
    <div class="container" >

        <div v-if="displayContent">

            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля название и дата должны быть обязательно заполнены</p>

            <div  class="labelm" >
                <div class="row justify-content-md-center ">
                    <h5 class="text-center fs-4 col-md-5 labelm" >Добавление новой записи</h5>
                </div>
                
                <div class="row justify-content-md-center ">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
                            <label for="inputName" class="form-label">Название</label>
                            <input type="text" class="form-control" id="inputName"  v-model="name"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputName" class="form-label">Описание</label>
                            <input type="text" class="form-control" id="inputName"  v-model="description"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputDateBirth" class="form-label">Дата </label>
                            <input type="datetime-local" class="form-control" id="inputDateBirth"  v-model="date"> 
                        </div>
                        <button type="button" class="btn btn-outline-success " @click="addPrompt()">Добавить запись</button>
                        <div class="alert alert-success" role="alert" v-if="showAlert">
                            Успешно!
                        </div>
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

import { ref, computed, onMounted } from 'vue';
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import UserService from '../../services/user.service';

    const store = useStore();
    const router = useRouter();
    const currentUser = computed(() => store.state.auth.user);

    const name= ref(null)
    const description= ref(null)
    const date= ref(null)

    // const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);

    const displayContent= ref(false)
    const content = ref('')


    async function addPrompt() {
        if (name.value && date.value) {
            isFormValid.value = true;
            try {
                if(description.value == null){
                    description.value =""
                }
                var a = {
                    userId: currentUser.value.id,
                    name: name.value,
                    date: date.value,
                    description: description.value,
                    calendar_id: null
                };
                await http.put('/addPrompt/' + currentUser.value.id, a);
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                }, 1000);
            } catch (error) {
                // console.error(error);
                if(error.response.status == 401 || error.response.status == 403){
                await store.dispatch('auth/logout')
                router.push('/login')
                }
            }
        } else {isFormValid.value = false;}
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
    });
  

</script>


<style>

.labelm {
    margin-top: 20px;
}

.bthM{
    margin: 20px;
}


</style>