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

export default {
  name: 'ProfileAgency',
  components: {
    MyProfileMyPage,
    MyProfileForm,
    MyProfileFavoriteAnimal,
  }
}
</script>
<style>
  
</style>