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