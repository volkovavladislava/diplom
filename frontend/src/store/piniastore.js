import { defineStore } from 'pinia'

export const usePiniastore = defineStore('piniastore', {
  state: () => ({
    guestdata: [],
    kindOfMarkData: [],
    kindOfMarkPersonalData: []
  }),
  actions: {
    setGuestdata(newValue) {
      this.guestdata = newValue
    },
    setKindOfMarkData(newValue) {
      this.kindOfMarkData = newValue
    },
    setKindOfMarkPersonalData(newValue) {
      this.kindOfMarkPersonalData = newValue
    }
  },
  getters: {
    getGuestdata: (state) => state.guestdata,
    getKindOfMarkData: (state) => state.kindOfMarkData,
    getKindOfMarkPersonalData: (state) => state.kindOfMarkPersonalData,
  }
})