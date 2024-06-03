<template>
<div class="" style="overflow-x: hidden;">
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
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksAverageByDate()">Обновить данные</button>
                    &nbsp;
                    <button type="button" class="btn btn-outline-secondary" @click="getAdvice()" data-bs-toggle="modal" data-bs-target="#exampleModal">Показать совет</button>
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="i  in marks" :key="i.id"  @click="redirectToUpdaterecordMarkPage(i.id, i)">
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
                <div  style="margin-left: 200px; margin-top: 50px; width: 400px ">
                    <div class="">Установите параметр усреденения</div>
                    <br/>
                    <input type="number" class="form-control" id="inputHeight"   v-model="param"> 
                    <br/>
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksAverageByDateWithParam()">Обновить </button>
                </div>
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
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksAverageByDate()">Обновить данные</button>
                    <!-- &nbsp;
                    <button type="button" class="btn btn-outline-secondary" >Показать совет</button> -->
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="i  in marks" :key="i.id"  @click="redirectToUpdaterecordMarkPage(i.id, i)" >
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
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksAverageByDate()">Обновить данные</button>
                    &nbsp;
                    <button type="button" class="btn btn-outline-secondary" @click="getAdvice()" data-bs-toggle="modal" data-bs-target="#exampleModal">Показать совет</button>
                 </div>
                 <div class="">
                    <label  class="form-label labelm">Ваши записи:</label>

                    <div  class="col-md-12" v-if="marks.length">
                        <div class="card"    v-for="(i, index)  in marks" :key="i.id"  @click="redirectToUpdaterecordMarkPage(i.id, i)" >
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
             <div  style="margin-left: 200px; margin-top: 50px; width: 400px ">
                    <div class="">Установите параметр усреденения</div>
                    <br/>
                    <input type="number" class="form-control" id="inputHeight"   v-model="param"> 
                    <br/>
                    <button type="button" class="btn btn-outline-secondary" @click="getMarksAverageByDateWithParam()">Обновить </button>
                </div>
            </div>
    </div>



<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Рекомендация</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
      </div>
      <div class="modal-body">
        {{advice}}
        <br/>
        {{advice2}}
      </div>
      <!-- <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
        
      </div> -->
    </div>
  </div>
</div>


    
</div>
</template>

<script setup>

import { ref, onMounted,computed  } from 'vue';
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
import { useStore } from 'vuex';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

    const moment = require('moment');
    const router = useRouter();
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))

    // const userId = ref(1)
    const marks = ref([])
    const marks2 = ref([])

    const advice = ref(null)
    const advice2 = ref(null)

    const param = ref(2)


    const date1 = ref(null)
    const date2 = ref(null)

    // const listOfSituations = ref([
    //     { key: "спокойное", value: 1 },
    //     { key: "после нагрузки", value: 2 },
    //     { key: "после еды", value: 3 },
    //     { key: "после стресса", value: 4 },
    //     { key: "после сна", value: 5 },
    //     { key: "после приема лекарства", value: 6 }
    // ]);

    const listOfSituationsForLabel = ref([
        "спокойное",
        "после нагрузки" ,
        "после еды",
        "после стресса",
        "после сна",
        "после приема лекарства"
    ]);

    const metka = ref(0)
    const updatemetka = ref(1)

    const dataGraf = {
        labels: [],
        datasets: []
    }

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            x: {
            ticks: {
                // Наклон подписей по оси X
                callback: function(value) {
                return this.getLabelForValue(value)
                },
                autoSkip: false,
                maxRotation: 20,
                minRotation: 20
            }
            }
        },
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

    // const getMarks = async () => {
    //     try {
    //         const response = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id);
            
    //         const a = {
    //             label: data.value.name,
    //             backgroundColor: '#f87979',
    //             data: []
    //         }

    //         if(data.value.enum_kind_of_mark_id == 1){
    //             response.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value_number)
    //             })
    //             dataGraf.datasets.push(a)
    //             metka.value = 1
    //             marks.value.reverse()
    //         }

            
    //         if( data.value.enum_kind_of_mark_id == 4){
    //             response.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value)
    //             })
    //             dataGraf.datasets.push(a)
    //             metka.value = 1
    //             marks.value.reverse()
    //         }


            
    //         if( data.value.enum_kind_of_mark_id == 5){
    //             const response1 = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1);
    //             const response2 = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2);

    //             const a = {
    //                 label: "Систолоческое давление",
    //                 backgroundColor: '#f87979',
    //                 data: []
    //             }

    //             const b = {
    //                 label: "Диастолическое давление",
    //                 backgroundColor: '#f87979',
    //                 data: []
    //             }


    //             response1.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value_number)
    //             })
    //             response2.data.forEach(value => {
    //                 marks2.value.push(value)         
    //                 b.data.push(value.value_number)
    //             })

    //             dataGraf.datasets.push(a)
    //             dataGraf.datasets.push(b)
    //             metka.value = 1

    //             marks.value.reverse()
    //             marks2.value.reverse()

    //         }


            
    //     } catch (error) {
    //         console.error(error);
    //     }

    // }

    const getMarksAverage = async () => {
        try {
            
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }

            

            if(data.value.enum_kind_of_mark_id == 1){
                const response = await http.get('/marksForUserAverage/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id);
                const average = {
                    label: "Среднее",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average.data.push(value.moving_average)
                })
                dataGraf.datasets.push(a)
                dataGraf.datasets.push(average)
                metka.value = 1
                marks.value.reverse()
            }

            
            if( data.value.enum_kind_of_mark_id == 4){
                const response = await http.get('/marksForUserAverage/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id);
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_enum)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
                marks.value.reverse()
            }


            
            if( data.value.enum_kind_of_mark_id == 5){
                const response1 = await http.get('/marksForUserAverage/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 );
                const response2 = await http.get('/marksForUserAverage/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 );

                const a = {
                    label: "Систолоческое давление",
                    backgroundColor: '#f87979',
                    data: []
                }

                const b = {
                    label: "Диастолическое давление",
                    backgroundColor: '#c260f0',
                    data: []
                }

                const average1 = {
                    label: "Среднее систолическое",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                const average2 = {
                    label: "Среднее диастолическое",
                    backgroundColor: '#1f7d02',
                    data: [],
                    pointRadius: 5
                }


                response1.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average1.data.push(value.moving_average)
                })
                response2.data.forEach(value => {
                    marks2.value.push(value)         
                    b.data.push(value.value_number)
                    average2.data.push(value.moving_average)
                })

                dataGraf.datasets.push(a)
                dataGraf.datasets.push(b)
                dataGraf.datasets.push(average1)
                dataGraf.datasets.push(average2)
                metka.value = 1

                marks.value.reverse()
                marks2.value.reverse()

            }


            
        } catch (error) {
            console.error(error);
        }

    }

    const getMarksAverageByDate = async () => {
        try {
            metka.value = 0

            param.value =2 

            const d1 = ref(null)
            const d2 = ref(null)
            
            if(date1.value == null){
                d1.value = "1900-01-01 00:00"
            }
            else{
                d1.value = date1.value
            }
            if(date2.value == null){
                d2.value = "2030-01-01 00:00"
            }else{
                d2.value =date2.value
            }

            
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }

            if(data.value.enum_kind_of_mark_id == 1){
                const response = await http.get('/marksForUserAverageByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + d1.value + '/date2=' + d2.value);

                const average = {
                    label: "Среднее",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                marks.value=[]
                dataGraf.labels =[]
                a.data=[]
                average.data = []
                dataGraf.datasets=[]
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average.data.push(value.moving_average)
                })
                dataGraf.datasets.push(a)
                dataGraf.datasets.push(average)
                metka.value = 1
                marks.value.reverse()
            }

            
            if( data.value.enum_kind_of_mark_id == 4){
                const response = await http.get('/marksForUserAverageByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + d1.value + '/date2=' + d2.value);
                marks.value=[]
                dataGraf.labels =[]
                a.data=[]
                dataGraf.datasets=[]
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_enum)
                })
                dataGraf.datasets.push(a)
                metka.value = 1
                marks.value.reverse()
            }


            
            if( data.value.enum_kind_of_mark_id == 5){
            
                const response1 = await http.get('/marksForUserAverageByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 + '/date1=' + d1.value + '/date2=' + d2.value);
                const response2 = await http.get('/marksForUserAverageByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 + '/date1=' + d1.value + '/date2=' + d2.value);

                const a = {
                    label: "Систолоческое давление",
                    backgroundColor: '#f87979',
                    data: []
                }

                const b = {
                    label: "Диастолическое давление",
                    backgroundColor: '#c260f0',
                    data: []
                }
                const average1 = {
                    label: "Среднее систолическое",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                const average2 = {
                    label: "Среднее диастолическое",
                    backgroundColor: '#1f7d02',
                    data: [],
                    pointRadius: 5
                }
                marks.value=[]
                marks2.value=[]
                dataGraf.labels =[]
                a.data=[]
                b.data=[]
                average1.data=[]
                average2.data=[]
                dataGraf.datasets=[]

                response1.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average1.data.push(value.moving_average)
                })
                response2.data.forEach(value => {
                    marks2.value.push(value)         
                    b.data.push(value.value_number)
                    average2.data.push(value.moving_average)
                })

                dataGraf.datasets.push(a)
                dataGraf.datasets.push(b)
                dataGraf.datasets.push(average1)
                dataGraf.datasets.push(average2)
                metka.value = 1
                marks.value.reverse()
                marks2.value.reverse()
            }


            
        } catch (error) {
            console.error(error);
        }

    }

    
    // const getMarksByDate = async () => {
    //     try {
    //         metka.value = 0

    //         if(date1.value == null){
    //             date1.value = "1900-01-01 00:00"
    //         }
    //         if(date2.value == null){
    //             date2.value = moment.utc().format('YYYY-MM-DD HH:mm')  
    //         }

    //         const response = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + date1.value + '/date2=' + date2.value);
            
    //         const a = {
    //             label: data.value.name,
    //             backgroundColor: '#f87979',
    //             data: []
    //         }

    //         if(data.value.enum_kind_of_mark_id == 1){
    //             marks.value=[]
    //             dataGraf.labels =[]
    //             a.data=[]
    //             dataGraf.datasets=[]
    //             response.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value_number)
    //             })
    //             dataGraf.datasets.push(a)
    //             metka.value = 1
    //             marks.value.reverse()
    //         }

            
    //         if( data.value.enum_kind_of_mark_id == 4){
    //             marks.value=[]
    //             dataGraf.labels =[]
    //             a.data=[]
    //             dataGraf.datasets=[]
    //             response.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value)
    //             })
    //             dataGraf.datasets.push(a)
    //             metka.value = 1
    //             marks.value.reverse()
    //         }


            
    //         if( data.value.enum_kind_of_mark_id == 5){
            
    //             const response1 = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 + '/date1=' + date1.value + '/date2=' + date2.value);
    //             const response2 = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 + '/date1=' + date1.value + '/date2=' + date2.value);
    //             console.log(response1.data)
    //             console.log(response2.data)
    //             const a = {
    //                 label: "Систолоческое давление",
    //                 backgroundColor: '#f87979',
    //                 data: []
    //             }

    //             const b = {
    //                 label: "Диастолическое давление",
    //                 backgroundColor: '#f87979',
    //                 data: []
    //             }
    //             marks.value=[]
    //             marks2.value=[]
    //             dataGraf.labels =[]
    //             a.data=[]
    //             b.data=[]
    //             dataGraf.datasets=[]

    //             response1.data.forEach(value => {
    //                 marks.value.push(value)         
    //                 dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
    //                 a.data.push(value.value_number)
    //             })
    //             response2.data.forEach(value => {
    //                 marks2.value.push(value)         
    //                 b.data.push(value.value_number)
    //             })

    //             dataGraf.datasets.push(a)
    //             dataGraf.datasets.push(b)
    //             metka.value = 1
    //             marks.value.reverse()
    //             marks2.value.reverse()
    //         }


            
    //     } catch (error) {
    //         console.error(error);
    //     }

    // }
    

    const getAdvice = async () => {
        try {
            const d1 = ref(null)
            const d2 = ref(null)
            
            if(date1.value == null){
                d1.value = "1900-01-01 00:00"
            }
            else{
                d1.value = date1.value
            }
            if(date2.value == null){
                d2.value = "2030-01-01 00:00"
            }else{
                d2.value =date2.value
            }


            if(data.value.enum_kind_of_mark_id == 1){  
                const response = await http.get('/getAdvice/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + d1.value + '/date2=' + d2.value);
                advice.value = response.data.comment
                console.log(advice.value)
            }



            if( data.value.enum_kind_of_mark_id == 5){
                const response1 = await http.get('/getAdvice/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 + '/date1=' + d1.value + '/date2=' + d2.value);
                const response2 = await http.get('/getAdvice/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 + '/date1=' + d1.value + '/date2=' + d2.value);
                advice.value = response1.data.comment
                advice2.value = response2.data.comment
                
            }


            
        } catch (error) {
            console.error(error);
        }

    }


    const getMarksAverageByDateWithParam = async () => {
        try {
            metka.value = 0

            const d1 = ref(null)
            const d2 = ref(null)
            
            if(date1.value == null){
                d1.value = "1900-01-01 00:00"
            }
            else{
                d1.value = date1.value
            }
            if(date2.value == null){
                d2.value = "2030-01-01 00:00"
            }else{
                d2.value =date2.value
            }

            console.log(param.value)
            const response = await http.get('/marksForUserAverageByDateWithParam/userId=' + currentUser.value.id + '/kindOfMarkId=' + data.value.id + '/date1=' + d1.value + '/date2=' + d2.value + '/myparam=' + param.value);
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }

            if(data.value.enum_kind_of_mark_id == 1){
                const average = {
                    label: "Среднее",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                marks.value=[]
                dataGraf.labels =[]
                a.data=[]
                average.data = []
                dataGraf.datasets=[]
                response.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average.data.push(value.moving_average)
                })
                dataGraf.datasets.push(a)
                dataGraf.datasets.push(average)
                metka.value = 1
                marks.value.reverse()
            }

            
            // if( data.value.enum_kind_of_mark_id == 4){
            //     marks.value=[]
            //     dataGraf.labels =[]
            //     a.data=[]
            //     dataGraf.datasets=[]
            //     response.data.forEach(value => {
            //         marks.value.push(value)         
            //         dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
            //         a.data.push(value.value_enum)
            //     })
            //     dataGraf.datasets.push(a)
            //     metka.value = 1
            //     marks.value.reverse()
            // }


            
            if( data.value.enum_kind_of_mark_id == 5){
            
                const response1 = await http.get('/marksForUserAverageByDateWithParam/userId=' + currentUser.value.id + '/kindOfMarkId=' + 1 + '/date1=' + d1.value + '/date2=' + d2.value + '/myparam=' + param.value);
                const response2 = await http.get('/marksForUserAverageByDateWithParam/userId=' + currentUser.value.id + '/kindOfMarkId=' + 2 + '/date1=' + d1.value + '/date2=' + d2.value + '/myparam=' + param.value);

                const a = {
                    label: "Систолоческое давление",
                    backgroundColor: '#f87979',
                    data: []
                }

                const b = {
                    label: "Диастолическое давление",
                    backgroundColor: '#c260f0',
                    data: []
                }
                const average1 = {
                    label: "Среднее систолическое",
                    backgroundColor: '#9bf081',
                    data: [],
                    pointRadius: 5
                }

                const average2 = {
                    label: "Среднее диастолическое",
                    backgroundColor: '#1f7d02',
                    data: [],
                    pointRadius: 5
                }
                marks.value=[]
                marks2.value=[]
                dataGraf.labels =[]
                a.data=[]
                b.data=[]
                average1.data=[]
                average2.data=[]
                dataGraf.datasets=[]

                response1.data.forEach(value => {
                    marks.value.push(value)         
                    dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                    a.data.push(value.value_number)
                    average1.data.push(value.moving_average)
                })
                response2.data.forEach(value => {
                    marks2.value.push(value)         
                    b.data.push(value.value_number)
                    average2.data.push(value.moving_average)
                })

                dataGraf.datasets.push(a)
                dataGraf.datasets.push(b)
                dataGraf.datasets.push(average1)
                dataGraf.datasets.push(average2)
                metka.value = 1
                marks.value.reverse()
                marks2.value.reverse()
            }


            
        } catch (error) {
            console.error(error);
        }

    }

    const redirectToUpdaterecordMarkPage = (id, data) => {
        const encodedData = encodeURIComponent(JSON.stringify(data));
        router.push({ path: '/updateRecordMark/' + id, query: { data: encodedData } });
    };


onMounted(async () => {
    // await getMarks();
    await getMarksAverage();
});

</script>

<style scoped>

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