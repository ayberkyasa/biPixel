<template>
  <v-container class="pa-16">
    <v-row class="display-1">Request Movie</v-row>
    <v-row>
      <p
        class="my-4 px-2 py-2 grey lighten-2 red--text text--lighten-2 rounded-pill"
      >
        <v-icon large class="red--text text--lighten-2 mr-2"
          >mdi-alert-circle-outline</v-icon
        ><span
          >Directors, Actors, and Genre fields are multivalued so you can enter
          multi values separated with commas in these fields.</span
        >
      </p></v-row
    >
    <v-row>
      <v-col cols="12" md="4" class="pr-16">
        <v-text-field
          v-model="request.title"
          label="Title"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processDirectors"
          label="Directors"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processActors"
          label="Actors"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.production_year"
          label="Year"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.duration"
          label="Duration"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processGenre"
          label="Genre"
          outlined
          dense
          rounded
          :rules="[rules.required]"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row
      ><v-col cols="12" md="4" class="pr-16 text-center"
        ><v-btn color="green darken-1" rounded dark @click="requestMovie"
          >SUBMIT</v-btn
        ></v-col
      ></v-row
    >
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      color: "",
      text: "",
      snackbar: false,
      request: {
        title: "",
        directors_full_name: [],
        actors_full_name: [],
        production_year: "",
        duration: null,
        genre: [],
      },
      rules: {
        required: (value) => !!value || "Required!",
      },
    };
  },
  methods: {
    processActors(e) {
      if (e.target.value == "") this.request.actors_full_name = [];
      else {
        this.request.actors_full_name = e.target.value
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processDirectors(e) {
      if (e.target.value == "") this.request.directors_full_name = [];
      else {
        this.request.directors_full_name = e.target.value
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processGenre(e) {
      if (e.target.value == "") this.request.genre = [];
      else {
        this.request.genre = e.target.value.split(",").map((value) => {
          return value.trim();
        });
      }
    },
    print() {
      console.log(this.request);
    },
  },
};
</script>
