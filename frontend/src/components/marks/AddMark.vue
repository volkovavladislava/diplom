<template>
    <div class="container" >
        <div v-if="displayContent">
            <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля должны быть обязательно заполнены</p>

            <div v-if="data.enum_kind_of_mark_id == 1" class="labelm" >
                <div class="row justify-content-md-center labelm">
                    <h5 class="text-center fs-3 col-md-5">Показатель {{data.name }}</h5>
                </div>
                
                <div class="row justify-content-md-center labelm">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
                            <label for="inputWeight" class="form-label">Значение</label>
                            <input type="number" class="form-control" id="inputWeight"  v-model="value1"> 
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
                        <button type="button" class="btn btn-outline-success" @click="addMark()">Добавить данные</button>
                        <div class="alert alert-success" role="alert" v-if="showAlert">
                            Успешно!
                        </div>
                    </div>
                </div>
            </div>



            <div v-if="data.enum_kind_of_mark_id == 4" class="labelm">
                <div class="row justify-content-md-center labelm">
                    <h5 class="text-center fs-3 col-md-5">Показатель {{data.name }}</h5>
                </div>
                <div class="row justify-content-md-center labelm">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
                            <label for="inputSelect" class="form-label">Значение</label>
                            <select id="inputSelect"  class="form-control" v-model="value1">
                                <option v-for="i  in enumValues" :key="i.value">{{ i.key }} </option>
                            </select>
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
                         <div class="mb-3 labelm">
                            <button type="button" class="btn btn-outline-success " @click="addMarkEnum()">Добавить данные</button>
                            <div class="alert alert-success" role="alert" v-if="showAlert">
                                Успешно!
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>

            <div v-if="data.enum_kind_of_mark_id == 5" class="labelm">
                <div class="row justify-content-md-center labelm">
                    <h5 class="text-center fs-3 col-md-5">Показатель {{data.name }}</h5>
                </div>
                <div class="row justify-content-md-center labelm">
                    <div  class="col-md-3 labelm">
                        <div class="mb-3 labelm">
                            <label for="inputWeight" class="form-label">Значение систолическое</label>
                            <input type="number" class="form-control" id="inputWeight"  v-model="value1"> 
                        </div>
                        <div class="mb-3 labelm">
                            <label for="inputWeight" class="form-label">Значение диастолическое</label>
                            <input type="number" class="form-control" id="inputWeight"  v-model="value2"> 
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
                        <button type="button" class="btn btn-outline-success " @click="addMarkDavlenie()">Добавить данные</button>
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

import { ref, onMounted,computed  } from 'vue';
import { useRouter, useRoute } from 'vue-router'
import http from "../../http-common";
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useStore } from 'vuex';
  import UserService from '../../services/user.service';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

    const router = useRouter();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))
    const route = useRoute();
    const id = ref(route.params.id)

    const value1= ref(null)
    const value2= ref(null)
    const date= ref(null)
    const situation= ref(null)

  const displayContent= ref(false)
  const content = ref('')

    // const userId = ref(1)

    const isFormValid = ref(true);
    const showAlert = ref(false);


    const enumValues = ref([]);

    const listOfSituations = ref([
        { key: "спокойное", value: 1 },
        { key: "после нагрузки", value: 2 },
        { key: "после еды", value: 3 },
        { key: "после стресса", value: 4 },
        { key: "после сна", value: 5 },
        { key: "после приема лекарства", value: 6 }
    ]);

    const getEnumvalues = async () => {
        try {
            const response = await http.get('/kindOfMarkValues/' + id.value);
            response.data.forEach(value => {
                enumValues.value.push({key: value.value, value: value.id})
            })

            console.log(enumValues.value)
        } catch (error) {
            // console.error(error);
                if(error.response.status == 401 || error.response.status == 403){
                await store.dispatch('auth/logout')
                router.push('/login')
                }
        }

    }


    async function addMark() {
        if (value1.value && date.value && situation.value) {
            isFormValid.value = true;
            // console.log( data)
            try {
                const s = listOfSituations.value.find(item => item.key === situation.value)
                var a = {
                    userId: currentUser.value.id,
                    kind_of_mark_id: data.value.id,
                    date: date.value,
                    situation: s.value,
                    value_number: value1.value,
                    value_string: null,
                    value_enum: null
                };
                await http.put('/addMark/' + data.value.id, a);
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

    async function addMarkDavlenie() {
        if (value1.value && date.value && value2.value && situation.value) {
            isFormValid.value = true;
            try {
                const s = listOfSituations.value.find(item => item.key === situation.value)
                var a = {
                    userId: currentUser.value.id,
                    date: date.value,
                    situation: s.value,
                    value_number1: value1.value,
                    value_number2: value2.value,
                };
                await http.put('/addMarkD' , a);
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

    async function addMarkEnum() {
        if (value1.value && date.value && situation.value) {
            isFormValid.value = true;
            const b = enumValues.value.find(item => item.key === value1.value)
            
            try {
                const s = listOfSituations.value.find(item => item.key === situation.value)
                var a = {
                    userId: currentUser.value.id,
                    kind_of_mark_id: data.value.id,
                    date: date.value,
                    situation: s.value,
                    value_number: null,
                    value_string: null,
                    value_enum: b.value
                };
                await http.put('/addMark/' + data.value.id, a);
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
    await getEnumvalues();
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