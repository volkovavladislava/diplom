<template>

    <div  class="container labelm">

        <div class="row justify-content-md-center labelm">
            <h4 class="text-center fs-3 col-md-5">Данные профиля</h4>
        </div>

        <div class="row justify-content-md-center labelm">
            

            <div  class="col-md-3 labelm">
                <div  class="mb-3 ">
                    <label for="inputName" class="form-label">Имя</label>
                    <input type="text" class="form-control" id="inputName"  v-model="user.name"> 
                </div>
                <div class="mb-3 labelm">
                    <label for="inputHeight" class="form-label">Рост</label>
                    <input type="number" class="form-control" id="inputHeight"  v-model="user.height"> 
                </div>
                <div class="mb-3 labelm">
                    <label for="inputWeight" class="form-label">Вес</label>
                    <input type="number" class="form-control" id="inputWeight"  v-model="user.weight"> 
                </div>
                <div class="mb-3 labelm">
                    <label for="inputDateBirth" class="form-label">Дата рождения</label>
                    <input type="date" class="form-control" id="inputDateBirth"  v-model="user.date_birth"> 
                </div>
                <p v-if="!isFormValid" style="color: #eb4034;">Поля имя, рост, вес и дата рождения должны быть обязательно заполнены</p>

                <button type="button" class="btn btn-outline-success " @click="updateUser()">Обновить данные</button>
            </div>

            <div class="row justify-content-md-center labelm">
                <h4 class="text-center fs-3 col-md-5">Данные рабочих показателей</h4>
            </div>

            <div  class="col-md-3">
                <div class="mb-3 labelm" >
                    <label for="inputName" class="form-label">Систолическое давление</label>
                    <input type="number" class="form-control" id="inputName"   v-model="profilePressureSystolic"> 
                </div>
                <div class="mb-3 labelm" >
                    <label for="inputHeight" class="form-label">Диастолическое давление</label>
                    <input type="number" class="form-control" id="inputHeight"   v-model="profilePressureDiastolic"> 
                </div>
                <div class="mb-3 labelm" >
                    <label for="inputWeight" class="form-label">Пульс</label>
                    <input type="number" class="form-control" id="inputWeight"   v-model="profilePulse"> 
                </div>
                <div class="mb-3 labelm" >
                    <label for="inputDateBirth" class="form-label">Сахар</label>
                    <input type="number" class="form-control" id="inputDateBirth"   v-model="profileSugar"> 
                </div>
                <div class="mb-3 labelm" >
                    <label for="inputDateBirth" class="form-label">Холестерин</label>
                    <input type="number" class="form-control" id="inputDateBirth"   v-model="profileCholesterol"> 
                </div>
                <button type="button" class="btn btn-outline-success " @click="updateUserOperatingValue()">Обновить рабочие показатели</button>
            </div>
        </div>

        

    </div>
    
</template>

<script setup>
import { ref, onMounted } from 'vue';
import http from "../../http-common";


const user = ref({
    name: null,
    height: null,
    weight: null,
    date_birth: null,
    gender: null,
    password:null,
    login: null
})


const profilePressureSystolic = ref(null);
const profilePressureDiastolic = ref(null);
const profilePulse = ref(null);
const profileSugar = ref(null);
const profileCholesterol = ref(null);

const isFormValid = ref(true);





const getUser = async () => {
    try {
        const response = await http.get('/user/' + 1);
        user.value.name = response.data.name;
        user.value.height = response.data.height;
        user.value.weight = response.data.weight;
        user.value.date_birth = response.data.date_birth;
        user.value.gender = response.data.gender;
        user.value.password = response.data.password;
        user.value.login = response.data.login;
    } catch (error) {
        console.error(error);
    }
}

const getUserOperatingValue = async () => {
    try {
        const response = await http.get('/userOpertingValue/' + 1);
         response.data.forEach(value => {
            switch(value.kind_of_mark_id) {
                case 1:
                profilePressureSystolic.value = value.value;
                break;
                case 2:
                profilePressureDiastolic.value = value.value;
                break;
                case 3:
                profilePulse.value = value.value;
                break;
                case 4:
                profileSugar.value = value.value;
                break;
                case 5:
                profileCholesterol.value = value.value;
                break;

            }
            
        });
    } catch (error) {
        console.error(error);
    }
}


async function updateUser() {
    if (user.value.name && user.value.height && user.value.weight && user.value.date_birth) {
        isFormValid.value = true;
        try {
            var data = {
                name: user.value.name,
                height: user.value.height,
                weight: user.value.weight,
                date_birth:  user.value.date_birth,
                gender: user.value.gender,
                password: user.value.password,
                login: user.value.login
            };
            await http.put('/updateUser/' + 1, data);
            
        } catch (error) {
            console.error(error);
        }
    } else {isFormValid.value = false;}
    
    
}


async function updateUserOperatingValue() {
        const currentDate = new Date();
        try {
            var data = {
                user_id: 1,
                value1:profilePressureSystolic.value,
                value2:profilePressureDiastolic.value,
                value3:profilePulse.value,
                value4:profileSugar.value,
                value5:profileCholesterol.value,
                date:  currentDate.toLocaleDateString()
            };
            await http.put('/updateUserOperatingValue/' + 1, data);

           
        } catch (error) {
            console.error(error);
        } 
    
}


onMounted(async () => {
    await getUser();
    await getUserOperatingValue();
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