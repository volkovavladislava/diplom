<template>
<div class="" style="overflow-x: hidden;">
    <button type="button" class="btn btn-outline-danger top-left-button" @click="logOutGuest()">Выйти из гостевого доступа</button>

    <div class="row justify-content-md-center " style="margin-top: 20px">
            <h5 class="text-center fs-4 col-md-5 " >Статистика по показателю {{data.name}}</h5>
        </div>

    <div class="container1"  v-if="metka && data.enum_kind_of_mark_id == 1">
            <div class=" left">
                
                 <div class="mb-3 ">
                    <label  class="form-label">Выберите период, по которому вывести статистику</label>
                    <input type="datetime-local" class="form-control" id="inputDate1"  v-model="date1"> 
                    <br/>
                    <input type="datetime-local" class="form-control" id="inputDate2"  v-model="date2"> 
                 </div>
                 <div class="">
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksByDate()">Обновить данные</button>
                    &nbsp;
                    <button type="button" class="btn btn-outline-secondary" >Показать совет</button>
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="i  in marks" :key="i.id" >
                                <div class="card-content">
                                <div class="row align-items-center">
                                    <div class="col">
                                        {{ moment.utc(i.date).format('YYYY-MM-DD HH:mm')}}
                                    </div>
                                    <div class="col ">
                                        {{i.value_number}}
                                    </div>
                                    <div class="col ">
                                        {{ listOfSituationsForLabel[i.situation-1] }}
                                    </div>
                                </div>
                                </div>
                            </div>
                    </div>
                 </div>
            </div>

            <div  class=" right" style="margin-left: 40px; margin-top: 8px" v-if="updatemetka">
             <Line :data="dataGraf" :options="options" v-model="dataGraf.datasets" style="width: 700px; max-height: 300px;"/>
            </div>
    </div>

    <div class="container1"  v-if="metka && data.enum_kind_of_mark_id == 4">
            <div class=" left">
                
                 <div class="mb-3 ">
                    <label  class="form-label">Выберите период, по которому вывести статистику</label>
                    <input type="datetime-local" class="form-control" id="inputDate1"  v-model="date1"> 
                    <br/>
                    <input type="datetime-local" class="form-control" id="inputDate2"  v-model="date2"> 
                 </div>
                 <div class="">
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksByDate()">Обновить данные</button>
                    &nbsp;
                    <button type="button" class="btn btn-outline-secondary" >Показать совет</button>
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="i  in marks" :key="i.id"  >
                                <div class="card-content">
                                <div class="row align-items-center">
                                    <div class="col">
                                        {{ moment.utc(i.date).format('YYYY-MM-DD HH:mm')}}
                                    </div>
                                    <div class="col ">
                                        {{i.value}}
                                    </div>
                                    <div class="col ">
                                        {{ listOfSituationsForLabel[i.situation-1] }}
                                    </div>
                                </div>
                                </div>
                            </div>
                    </div>
                 </div>
            </div>

            <div  class=" right" style="margin-left: 40px; margin-top: 8px">
             <Line :data="dataGraf" :options="options" v-model="dataGraf" style="width: 700px; max-height: 300px;"/>
            </div>
    </div>

    <div class="container1"  v-if="metka && data.enum_kind_of_mark_id == 5">
            <div class=" left">
                
                 <div class="mb-3 ">
                    <label  class="form-label">Выберите период, по которому вывести статистику</label>
                    <input type="datetime-local" class="form-control" id="inputDate1"  v-model="date1"> 
                    <br/>
                    <input type="datetime-local" class="form-control" id="inputDate2"  v-model="date2"> 
                 </div>
                 <div class="">
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksByDate()">Обновить данные</button>
                    &nbsp;
                    <button type="button" class="btn btn-outline-secondary" >Показать совет</button>
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="(i, index)  in marks" :key="i.id"  >
                                <div class="card-content">
                                <div class="row align-items-center">
                                    <div class="col" style="min-width: 120px;">
                                        {{ moment.utc(i.date).format('YYYY-MM-DD HH:mm')}}
                                    </div>
                                    <div class="col">
                                        {{i.value_number}}
                                    </div>
                                    <div class="col">
                                        {{marks2[index].value_number}}
                                    </div>
                                    <div class="col ">
                                        {{ listOfSituationsForLabel[i.situation-1] }}
                                    </div>
                                </div>
                                </div>
                            </div>
                    </div>
                 </div>
            </div>

            <div  class=" right" style="margin-left: 40px; margin-top: 8px">
             <Line :data="dataGraf" :options="options" v-model="dataGraf" style="width: 700px; max-height: 300px;"/>
            </div>
    </div>
</div>
</template>

<script setup>

import { ref, onMounted  } from 'vue';
import { useRouter } from 'vue-router'
import http from "../../http-common";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js'
import { Line } from 'vue-chartjs'


    const moment = require('moment');
    const router = useRouter();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data1, null, 2)))
    const currentUser = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data2, null, 2)))

    // const userId = ref(1)
    const marks = ref([])
    const marks2 = ref([])


    const date1 = ref(null)
    const date2 = ref(null)

    const metka = ref(0)
    const updatemetka = ref(1)

    const dataGraf = {
        labels: [],
        datasets: []
    }

    const options = {
        responsive: true,
        maintainAspectRatio: false
    }


    ChartJS.register(
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    )


    const listOfSituationsForLabel = ref([
        "спокойное",
        "после нагрузки" ,
        "после еды",
        "после стресса",
        "после сна",
        "после приема лекарства"
    ]);
    

    const getMarks = async () => {
        try {
            const response = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id);
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }

            if(data.value.enum_kind_of_mark_id == 1){
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
                marks.value.reverse()
            }

            
            if( data.value.enum_kind_of_mark_id == 4){
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
                marks.value.reverse()
            }


            
            if( data.value.enum_kind_of_mark_id == 5){
                const response1 = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1);
                const response2 = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2);

                const a = {
                    label: "Систолоческое давление",
                    backgroundColor: '#f87979',
                    data: []
                }

                const b = {
                    label: "Диастолическое давление",
                    backgroundColor: '#f87979',
                    data: []
                }


                response1.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                })
                response2.data.forEach(value => {
                    marks2.value.push(value)         
                    b.data.push(value.value_number)
                })

                dataGraf.datasets.push(a)
                dataGraf.datasets.push(b)
                metka.value = 1

                marks.value.reverse()
                marks2.value.reverse()

            }


            
        } catch (error) {
            console.error(error);
        }

    }

    
    const getMarksByDate = async () => {
        try {
            metka.value = 0

            if(date1.value == null){
                date1.value = "1900-01-01 00:00"
            }
            if(date2.value == null){
                date2.value = moment.utc().format('YYYY-MM-DD HH:mm')  
            }

            const response = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + date1.value + '/date2=' + date2.value);
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }

            if(data.value.enum_kind_of_mark_id == 1){
                marks.value=[]
                dataGraf.labels =[]
                a.data=[]
                dataGraf.datasets=[]
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
                
            }

            
            if( data.value.enum_kind_of_mark_id == 4){
                marks.value=[]
                dataGraf.labels =[]
                a.data=[]
                dataGraf.datasets=[]
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
            }


            
            if( data.value.enum_kind_of_mark_id == 5){
            
                const response1 = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 + '/date1=' + date1.value + '/date2=' + date2.value);
                const response2 = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 + '/date1=' + date1.value + '/date2=' + date2.value);
                console.log(response1.data)
                console.log(response2.data)
                const a = {
                    label: "Систолоческое давление",
                    backgroundColor: '#f87979',
                    data: []
                }

                const b = {
                    label: "Диастолическое давление",
                    backgroundColor: '#f87979',
                    data: []
                }
                marks.value=[]
                marks2.value=[]
                dataGraf.labels =[]
                a.data=[]
                b.data=[]
                dataGraf.datasets=[]

                response1.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                })
                response2.data.forEach(value => {
                    marks2.value.push(value)         
                    b.data.push(value.value_number)
                })

                dataGraf.datasets.push(a)
                dataGraf.datasets.push(b)
                metka.value = 1

            }


            
        } catch (error) {
            console.error(error);
        }

    }
    

    const logOutGuest = async () => {
      try {
            var a = {
                id: currentUser.value.id 
            };
            await http.post('/logoutGuest', a);
            router.push({ path: '/guestMode' });
          
      } catch (error) {
          console.error(error);
      }
  }

  const checkGuest = async () => {
        try {
            var a = {
                login: currentUser.value.login,
            };
             await http.post('/checkGuest' , a);
        } catch (error) {
            if(error.response.status == 401 || error.response.status == 404 || error.response.status == 403){
                router.push({ path: '/guestMode' });
            }
        }
    }

    


onMounted(async () => {
    await checkGuest();
    await getMarks();
});

</script>

<style scoped>
.top-left-button {
    position: absolute;
    top: 650px;
    left: 1250px;
}


.container1 {
  display: flex;
  /* display: grid; */
  margin: 30px;
 
  
}

.left {
 flex-grow: 1; 
  /* overflow-y: auto; */
  /*  grid-column: 1; */
  max-width: 400px;
  margin-left: 150px;
  margin-right: 160px;
}

.right {
  flex-shrink: 0;
  /* Или grid-column: 2;  */
}

</style>