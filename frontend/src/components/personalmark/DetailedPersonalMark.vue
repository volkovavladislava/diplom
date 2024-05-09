<template>
    <div class="container labelm">
        <div class="row justify-content-md-center labelm">

            <div class="row justify-content-md-center labelm">
                <h5 class="text-center fs-4 col-md-5"> Статистика по показателю {{data.name }}</h5>
            </div>
            <div  class="col-md-7" v-if="marks.length">
                <div class="card"    v-for="i  in marks" :key="i.id" @click="redirectToUpdatePersonalMarkPage(i.id, i)">
                        <div class="card-content">
                        <div class="row align-items-center">
                            <div class="col">
                           {{ moment(i.date).format('YYYY-MM-DD HH:mm')}}
                            </div>
                            <div class="col ">
                            {{i.value_string}}
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

  import { useRouter,useRoute } from 'vue-router';

  const moment = require('moment');
  const userId = ref(1)

  const router = useRouter();
  const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))
  const route = useRoute();
  const id = ref(route.params.id)

  const marks = ref([])


  const redirectToUpdatePersonalMarkPage = (id, data) => {
    console.log(data)
    const encodedData = encodeURIComponent(JSON.stringify(data));
    router.push({ path: '/updateRecordPersonalMark/' + id, query: { data: encodedData } });
  };

 const getListRecordsMark = async () => {
      try {
          const response = await http.get('/marksForUser/userId=' + userId.value + '/kindOfMarkId=' + id.value);
          marks.value = []
          response.data.forEach(value => {
                marks.value.push(value)         
              
          });

          console.log(marks.value.length)
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