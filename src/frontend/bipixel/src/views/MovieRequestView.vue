<template>
  <v-container class="pa-16">
    <v-row class="display-1">Request Movie</v-row>
    <v-row>
      <p
        class="
          my-4
          px-2
          py-2
          grey
          lighten-2
          red--text
          text--lighten-2
          rounded-pill
        "
      >
        <v-icon large class="red--text text--lighten-2 mr-2"
          >mdi-alert-circle-outline</v-icon
        ><span
          >Directors, Actors, and Genres fields are multivalued so you can enter
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
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.directors_full_name"
          label="Directors"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.actors_full_name"
          label="Actors"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.production_year"
          label="Year"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.duration"
          label="Duration"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-select
          :items="friends"
          v-model="selectedFriend"
          :menu-props="{ top: true, offsetY: true }"
          label="Select a Genre"
          item-text="full_name"
          return-object
        ></v-select>
        <v-text-field
          v-model="request.genres"
          label="Genres"
          outlined
          dense
          rounded
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
    <v-snackbar :color="color" timeout="2000" v-model="snackbar">
      {{ text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      color: "",
      text: "",
      snackbar: false,
      request: {
        title: "",
        directors_full_name: "",
        actors_full_name: "",
        production_year: "",
        duration: "",
        genres: "",
      },
      rules: {
        required: (value) => !!value || "Required!",
      },
    };
  },
  methods: {
    processActors() {
      if (this.request.actors_full_name === "")
        this.request.actors_full_name = "";
      else {
        this.request.actors_full_name = this.request.actors_full_name
          .split(",")
          .map((value) => {
            return value.trim();
          })
          .toString();
      }
    },
    processDirectors() {
      if (this.request.directors_full_name === "")
        this.request.directors_full_name = "";
      else {
        this.request.directors_full_name = this.request.directors_full_name
          .split(",")
          .map((value) => {
            return value.trim();
          })
          .toString();
      }
    },
    processGenre() {
      if (this.request.genres === "") this.request.genres = "";
      else {
        this.request.genres = this.request.genres
          .split(",")
          .map((value) => {
            return value.trim();
          })
          .toString();
      }
    },
    async requestMovie() {
      if (
        this.request.title === "" ||
        this.request.directors_full_name === "" ||
        this.request.actors_full_name === "" ||
        this.request.production_year === "" ||
        this.request.duration === "" ||
        this.request.genres === ""
      ) {
        this.text = "All fields must be filled.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      this.processActors();
      this.processDirectors();
      this.processGenre();
      this.request.duration = Number(this.request.duration);
      if (isNaN(this.request.duration)) {
        this.text = "Field types are not correct.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      try {
        const res = await axiosInstance.post(URL.REQUEST_MOVIE, {
          ...this.request,
          userId: this.$store.state.uid,
        });
        this.request = {
          title: "",
          directors_full_name: "",
          actors_full_name: "",
          production_year: "",
          duration: "",
          genres: "",
        };
        this.text = res.data.result;
        this.color = "green lighten-1";
        this.snackbar = true;
      } catch (error) {
        this.text = error.response.data.result;
        this.color = "red darken-1";
        this.snackbar = true;
      }
    },
  },
};
</script>
