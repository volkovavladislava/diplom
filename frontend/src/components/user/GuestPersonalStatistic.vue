<template>
    <div class="container labelm">
        <button type="button" class="btn btn-outline-danger top-left-button" @click="logOutGuest()">Выйти из гостевого доступа</button>

        <div class="row justify-content-md-center labelm">

            <div class="row justify-content-md-center labelm">
                <h5 class="text-center fs-4 col-md-5"> Статистика по показателю {{kindOfMarkData.name }}</h5>
            </div>
            <div  class="col-md-7" v-if="marks.length">
                <div class="card"    v-for="i  in marks" :key="i.id">
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

  import { ref, onMounted } from 'vue';
  import http from "../../http-common";
  import { useRouter } from 'vue-router';

    

  const moment = require('moment');

  const router = useRouter();
  const kindOfMarkData = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data1, null, 2)))
  const currentUser = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data2, null, 2)))

    const listOfSituationsForLabel = ref([
        "спокойное",
        "после нагрузки" ,
        "после еды",
        "после стресса",
        "после сна",
        "после приема лекарства"
    ]);

  const marks = ref([])


 const getListRecordsMark = async () => {
      try {
          const response = await http.get('/marksForUser/userId=' + currentUser.value.id + '/kindOfMarkId=' + kindOfMarkData.value.id);
          marks.value = []
          response.data.forEach(value => {
                marks.value.push(value)         
          });
          marks.value.reverse()

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
    await getListRecordsMark();
});

</script>

<style>
.top-left-button {
    position: absolute;
    top: 650px;
    left: 1250px;
}

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