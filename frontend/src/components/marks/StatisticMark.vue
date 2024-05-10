<template>
<div class="" style="overflow-x: hidden;">
    <div class="row justify-content-md-center " style="margin-top: 20px">
            <h5 class="text-center fs-4 col-md-5 " >Статистика по показателю {{data.name}}</h5>
        </div>

    <div class="container1"  v-if="metka">
    <!-- <div  class=""  > -->
        <!-- <div class="row justify-content-md-center " style="margin-top: 20px">
            <h5 class="text-center fs-4 col-md-5 labelm" >Статистика по показателю {{data.name}}</h5>
        </div> -->
        <!-- <div class=""  style="margin-top: 40px"> -->

            <div class=" left">
                
                 <div class="mb-3 ">
                    <label  class="form-label">Выберите период, по которому вывести статистику</label>
                    <input type="datetime-local" class="form-control" id="inputDate1"  v-model="date1"> 
                    <br/>
                    <input type="datetime-local" class="form-control" id="inputDate2"  v-model="date2"> 
                 </div>
                 <div class="">
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
                                        {{i.value_number}}
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
        <!-- </div> -->

        
    <!-- </div> -->
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
    const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))
    // const route = useRoute();
    // const id = ref(route.params.id)

    const userId = ref(1)
    const marks = ref([])

    const date1 = ref(null)
    const date2 = ref(null)

    const metka = ref(0)

    const dataGraf = {
        labels: [],
        datasets: []
    }

    const options = {
        responsive: true,
        maintainAspectRatio: false
    }

    

    // const dataGraf = {
    //     labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    //     datasets: [
    //         {
    //         label: 'Data One',
    //         backgroundColor: '#f87979',
    //         data: [40, 39, 10, 40, 39, 80, 40]
    //         }
    //     ]
    // }

    ChartJS.register(
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    )


    const getMarks = async () => {
        try {
            const response = await http.get('/marksForUser/userId=' + userId.value + '/kindOfMarkId=' + data.value.id);
            
            const a = {
                label: data.value.name,
                backgroundColor: '#f87979',
                data: []
            }
            response.data.forEach(value => {
                marks.value.push(value)         
                dataGraf.labels.push(moment.utc(value.date).format('YYYY-MM-DD HH:mm'))
                a.data.push(value.value_number)
            })
            dataGraf.datasets.push(a)
            console.log(dataGraf)
            metka.value = 1
        } catch (error) {
            console.error(error);
        }

    }

onMounted(async () => {
    await getMarks();
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
  margin-left: 180px;
  margin-right: 160px;
}

.right {
  flex-shrink: 0;
  /* Или grid-column: 2;  */
}

</style>