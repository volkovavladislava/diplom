<template>
    <div class="container" >

    
            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля значение и дата должны быть обязательно заполнены</p>

            <div  class="labelm" >
                <div class="row justify-content-md-center ">
                    <h5 class="text-center fs-4 col-md-5 labelm" >Персональный показатель {{data.name }}</h5>
                </div>
                
                <div class="row justify-content-md-center ">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
                            <label for="inputWeight" class="form-label">Значение</label>
                            <input type="text" class="form-control" id="inputWeight"  v-model="value1"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputDateBirth" class="form-label">Дата замера</label>
                            <input type="datetime-local" class="form-control" id="inputDateBirth"  v-model="date"> 
                        </div>
                        <button type="button" class="btn btn-outline-success " @click="addMark()">Добавить данные</button>
                        <div class="alert alert-success" role="alert" v-if="showAlert">
                            Успешно!
                        </div>
                    </div>
                </div>
            </div>

            
    </div>


</template>

<script setup>

import { ref  } from 'vue';
import { useRouter } from 'vue-router'
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

    const router = useRouter();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))

    const value1= ref(null)
    const date= ref(null)

    const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);


    async function addMark() {
        if (value1.value && date.value) {
            isFormValid.value = true;
            try {
                var a = {
                    userId: userId.value,
                    kind_of_mark_id: data.value.id,
                    date: date.value,
                    value_number: null,
                    value_string: value1.value,
                    value_enum: null
                };
                await http.put('/addMark/' + data.value.id, a);
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