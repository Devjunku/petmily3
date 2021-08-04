
import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate"
// import axios from 'axios'

export default createStore({
  plugins: [
    createPersistedState(),
  ],
  state: {
    email: "",
    isSignup: false,
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
