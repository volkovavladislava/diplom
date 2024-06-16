<template>
    <div class="container" >

        <div v-if="displayContent">
            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля значение,ситуация и дата должны быть обязательно заполнены</p>

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
                            <label for="inputSelect" class="form-label">Выберите ситуацию, после которой происходил замер</label>
                            <select id="inputSelect"  class="form-control" v-model="situation">
                                <option v-for="i  in listOfSituations" :key="i.value">{{ i.key }} </option>
                            </select>
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputDateBirth" class="form-label">Дата замера</label>
                            <input type="datetime-local" class="form-control" id="inputDateBirth"  v-model="date"> 
                        </div>
                        <button type="button" class="btn btn-outline-success " @click="updateMark()">Обновить запись</button>
                        <br/>
                        <br/>
                        <button type="button" class="btn btn-outline-danger" @click="deleteMark()">Удалить запись</button>
                        <div class="alert alert-success labelm" role="alert" v-if="showAlert">
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

import { ref,computed, onMounted  } from 'vue';
import { useRouter } from 'vue-router'
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useStore } from 'vuex';
import UserService from '../../services/user.service';
import moment from 'moment-timezone';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

    const listOfSituations = ref([
        { key: "спокойное", value: 1 },
        { key: "после нагрузки", value: 2 },
        { key: "после еды", value: 3 },
        { key: "после стресса", value: 4 },
        { key: "после сна", value: 5 },
        { key: "после приема лекарства", value: 6 }
    ]);

    // const moment = require('moment');

    const router = useRouter();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))


    const value1= ref(data.value.value_string)
    const date= ref( moment.utc(data.value.date).tz('Asia/Singapore').format('YYYY-MM-DD HH:mm'))
    const situation= ref((listOfSituations.value.find(item => item.value === data.value.situation)).key)

    // console.log(situation.value)

    // const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);

  const displayContent= ref(false)
  const content = ref('')
    

    async function updateMark() {
        if (value1.value && date.value && situation.value) {
            isFormValid.value = true;
            try {
                const s = listOfSituations.value.find(item => item.key === situation.value)
                var a = {
                    userId: currentUser.value.id,
                    kind_of_mark_id: data.value.kind_of_mark_id,
                    date: date.value,
                    situation: s.value,
                    value_number: null,
                    value_string: value1.value,
                    value_enum: null
                };
                await http.put('/updateMark/' + data.value.id, a);
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                    router.push({ path: '/listPersonalMarks'});
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

    async function deleteMark() {
            try {
                await http.post('/deleteMark/' + data.value.id);
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                    router.push({ path: '/listPersonalMarks'});
                }, 1000);
            } catch (error) {
                // console.error(error);
                if(error.response.status == 401 || error.response.status == 403){
                await store.dispatch('auth/logout')
                router.push('/login')
                }
            }
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