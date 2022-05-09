<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Favourite List</v-row>
    <v-data-table
      :headers="headers"
      :items="nonFriends"
      item-key="email"
      class="elevation-1"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          rounded
          small
          dark
          @click="showDetails(item)"
          @click.stop="detailsDialog = true"
          >Details</v-btn
        >
      </template>
      <template v-slot:[`item.remFav`]="{ item }">
        <v-btn
          class="mx-2"
          fab
          dark
          small
          color="pink"
          @click="remFavourites(item)"
        >
          <v-icon> mdi-close </v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Labguage Option: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: </strong>
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
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      showedMovie: {},
      detailsDialog: false,
      nonFriends: [
        {
          title: "Movie 1",
          director: "Director 1",
          genre: "Action",
          rate: "8",
          year: "2019",
          price: "10",
        },
        {
          title: "Movie 2",
          director: "Director 1",
          genre: "Comedy",
          rate: "8.5",
          year: "2018",
          price: "20",
        },
        {
          title: "Movie 3",
          director: "Director 2",
          genre: "Science Fiction",
          rate: "7",
          year: "2019",
          price: "150",
        },
        {
          title: "Movie 4",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "40",
        },
        {
          title: "Movie 5",
          director: "Director 2",
          genre: "Action",
          rate: "8",
          year: "2021",
          price: "5",
        },
        {
          title: "Movie 6",
          director: "Director 4",
          genre: "Science Fiction",
          rate: "8",
          year: "2019",
          price: "80",
        },
        {
          title: "Movie 7",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "65",
        },
        {
          title: "Movie 8",
          director: "Director 4",
          genre: "Comedy",
          rate: "8",
          year: "2019",
          price: "110",
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
          value: "director",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genre",
        },
        {
          text: "Rate",
          align: "start",
          value: "rate",
          sortable: false,
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "year",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
          sortable: false,
        },
        {
          text: "Details",
          align: "start",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "Remove Favourites",
          align: "start",
          value: "remFav",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    showDetails(value) {
      this.showedMovie = value;
    },
    select(value) {
      this.selected = value.value;
    },
    remFavourites(value) {
      console.log(value, "rem");
    },
  },
};
</script>
