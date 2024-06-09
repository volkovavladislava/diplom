<template>
    <div class="container labelm">
        <div class="row justify-content-md-center labelm">

            <div class="row justify-content-md-center labelm">
                <h5 class="text-center fs-4 col-md-5"> Статистика по показателю {{data.name }}</h5>
                
            </div>

            <div  class="row justify-content-md-center labelm" >
              <div  class="col-md-3" >
                <label  class="form-label">Выберите период,за который вывести статистику</label>
                <input type="datetime-local" class="form-control" id="inputDate1"  v-model="date1"> 
                <br/>
                <input type="datetime-local" class="form-control" id="inputDate2"  v-model="date2"> 
                <br/>
                <button type="button" class="btn btn-outline-secondary" @click="getListRecordsMarkByDate()">Обновить данные</button>
                <br/>
              </div>
            </div>

          

            <div  class="col-md-7" v-if="marks.length">
                <div class="card"    v-for="i  in marks" :key="i.id" @click="redirectToUpdatePersonalMarkPage(i.id, i)">
                        <div class="card-content">
                        <div class="row align-items-center">
                            <div class="col">
                           {{ moment.utc(i.date).format('YYYY-MM-DD HH:mm')}}
                            </div>
                            <div class="col ">
                            {{i.value_string}}
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
</template>


<script setup>

  import { ref, onMounted, computed } from 'vue';
  import http from "../../http-common";
  import { useRouter,useRoute } from 'vue-router';
  import { useStore } from 'vuex';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

  const moment = require('moment');
//   const userId = ref(1)

  const router = useRouter();
  const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))
  const route = useRoute();
  const id = ref(route.params.id)

  const marks = ref([])

    const date1 = ref(null)
    const date2 = ref(null)


  // const listOfSituations = ref([
  //       { key: "спокойное", value: 1 },
  //       { key: "после нагрузки", value: 2 },
  //       { key: "после еды", value: 3 },
  //       { key: "после стресса", value: 4 },
  //       { key: "после сна", value: 5 },
  //       { key: "после приема лекарства", value: 6 }
  //   ]);

    const listOfSituationsForLabel = ref([
        "спокойное",
        "после нагрузки" ,
        "после еды",
        "после стресса",
        "после сна",
        "после приема лекарства"
    ]);


  const redirectToUpdatePersonalMarkPage = (id, data) => {
    console.log(data)
    const encodedData = encodeURIComponent(JSON.stringify(data));
    router.push({ path: '/updateRecordPersonalMark/' + id, query: { data: encodedData } });
  };

 const getListRecordsMark = async () => {
      try {
          const response = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + id.value);
          marks.value = []
          response.data.forEach(value => {
                marks.value.push(value)         
              
          });
          marks.value.reverse()

      } catch (error) {
          console.error(error);
      }
  }


  const getListRecordsMarkByDate = async () => {
      try {
          const d1 = ref(null)
          const d2 = ref(null)
          
          if(date1.value == null || date1.value == ""){
              d1.value = "1900-01-01 00:00"
          }
          else{
              d1.value = date1.value
          }
          if(date2.value == null || date2.value == ""){
              d2.value = "2030-01-01 00:00"
          }else{
              d2.value =date2.value
          }
          

          const response = await http.get('/marksForUserByDate/userId=' + currentUser.value.id + '/kindOfMarkId=' + id.value + '/date1=' + d1.value + '/date2=' + d2.value );
          marks.value = []
          response.data.forEach(value => {
                console.log(value)
                marks.value.push(value)         
              
          });
          marks.value.reverse()

      } catch (error) {
          console.error(error);
      }
  }



  onMounted(async () => {
    await getListRecordsMark();
});

</script>

<style>

.labelm {
    margin-top: 30px;
    margin-bottom: 30px;
}

.bthM{
    margin: 20px;
}

.favorite{
  margin-left: 470px;
}

.card {
  border-radius: 8px;
  padding: 10px;
  background-color: #ffffff;
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  justify-content: space-between;
}

.col{
    width:400px !important;
    text-align: center;
    
}
</style>