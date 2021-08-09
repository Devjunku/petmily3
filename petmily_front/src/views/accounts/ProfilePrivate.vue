<template>
<div>
    <div>
      <MyProfileMyPage/>
    </div>
    <br/>
    <div class="container">
      <MyProfileForm/>
    </div>
    <br/>
    <div class="container">
      <MyProfileFavoriteAnimal/>
    </div>
  </div>
</template>
<script>
import MyProfileMyPage from '@/views/accounts/components/MyProfileMyPage'
import MyProfileFavoriteAnimal from '@/views/accounts/components/MyProfileFavoriteAnimal'
import MyProfileForm from '@/views/accounts/components/MyProfileForm'
import axios from "axios";

export default {
  name: 'ProfilePrivate',
  components: {
    MyProfileMyPage,
    MyProfileForm,
    MyProfileFavoriteAnimal,
  },
  setup () {

    const setToken = function () {
      const token = localStorage.getItem("jwt");
      const config = 'Bearer'+ ' ' + `${token}`
      console.log(config)
      return config
    }


    const getUserInfo = async function () {
      await axios({
        method: "get",
        url: "http://localhost:8080/users/personal/me",
        headers:{
          Authorization: setToken(),
        }
        })
        .then((res) => {
          console.log('51번째줄')
          console.log(res)
        })
        .catch((err) => {
          console.log('55번째줄')
          console.log(err)
        })
      }
    
    console.log(getUserInfo())
    return { getUserInfo }
  }
}
</script>
<style>
  
</style>