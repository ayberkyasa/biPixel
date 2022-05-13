<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Withdraw a Movie</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-row>
      <v-col cols="2"> Select Upper Threshold of Price: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="price"
          label="Enter a price"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="2"> Select Lower Threshold of Rate: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="rate"
          label="Enter a rate between 1 and 10"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
    </v-row>
    <v-data-table
      :headers="headers"
      :items="rentedMovies"
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="openDialog(item)"
          @click.stop="detailsDialog = true"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.addFav`]="{ item }">
        <v-btn icon color="pink" v-if="checkFav(item)">
          <v-icon>mdi-heart</v-icon>
        </v-btn>
        <v-btn
          icon
          color="blue-grey lighten-3"
          v-if="!checkFav(item)"
          @click="addFavorites(item)"
        >
          <v-icon>mdi-heart</v-icon>
        </v-btn>
      </template>
      <template v-slot:[`item.withdraw`]="{ item }">
        <v-btn
          color="purple lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="detailsDialog = true"
          >Withdraw</v-btn
        >
      </template>
      <template v-slot:[`item.renew`]="{ item }">
        <v-btn
          color="blue lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="renew"
          >Renew</v-btn
        >
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: {{ showedMovie.duration }} (min)</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: {{ showedMovie.language_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: {{ showedMovie.subtitle_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Actors: {{ actors }}</strong>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="detailsDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar :color="color" timeout="2000" v-model="snackbar">
      {{mes}}
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      snackbar: false,
      mes: "",
      color: "",
      rentedMovies: [],
      actors: "",
      showedMovie: {},
      detailsDialog: false,
      search: "",
      price: "",
      rate: "",
      selected: "None",
      selectElements: [
        {
          key: "none",
          value: "None",
        },
        {
          key: "action",
          value: "Action",
        },
        {
          key: "comedy",
          value: "Comedy",
        },
        {
          key: "sci_fi",
          value: "Science Fiction",
        },
        {
          key: "romance",
          value: "Romance",
        },
        {
          key: "mystery",
          value: "Mystery",
        },
      ],
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Director",
          align: "start",
          value: "directors",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
          filter: (value) => {
            if (this.selected === "None") {
              return true;
            } else {
              return value === this.selected;
            }
          },
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
          sortable: false,
          filter: (value) => {
            if (this.rate === "") {
              return true;
            } else {
              return value >= parseInt(this.rate);
            }
          },
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "production_year",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
          sortable: false,
          filter: (value) => {
            if (this.price === "") {
              return true;
            } else {
              return value <= parseInt(this.price);
            }
          },
        },
        {
          text: "Details",
          align: "center",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "",
          align: "center",
          value: "withdraw",
          filterable: false,
          sortable: false,
        },
        {
          text: "",
          align: "center",
          value: "renew",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    async renew() {
      console.log(this.showedMovie);
      try {
        const res = await axiosInstance.post(URL.RENEW, {
          rentId: this.showedMovie.movie_id,
          userId: this.$store.state.uid,
        });
        this.snackbar = true;
        this.mes = res.data.result;
        this.color = "green lighten-1";
        console.log(res);
      } catch (error) {
        this.snackbar = true;
        this.mes = "You can renew the rental period at most 3 times.";
        this.color = "red lighten-1";
        console.log(error);
      }
    },
    showActors() {
      this.showedMovie.actors.forEach((item) => {
        this.actors += item + ", ";
      });
    },
    openDialog(item) {
      this.showedMovie = item;
      this.showActors();
    },
  },
  async created() {
    console.log(this.search);
    try {
      const res = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
        params: {
          key: this.search,
          userId: this.$store.state.uid,
        },
      });
      this.rentedMovies = res.data;
    } catch (error) {
      console.log(error.response);
    }
  },
};
</script>
