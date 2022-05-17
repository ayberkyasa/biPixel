/* eslint-disable */
import axios from "axios";

const BASE_URL = "http://localhost:8081";

const CONTENT_TYPE = "application/json";
const ACCEPTS = "application/json";

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": CONTENT_TYPE,
    "Accept": ACCEPTS
  },
});

export default axiosInstance;

export const URL = {
    LOGIN: "login",
    REGISTER: "register",
    GET_USER: "get-user",
    ADD_MOVIE: "add-movie",
    REMOVE_MOVIE: "remove-movie",
    DELETE_CUSTOMER: "delete-customer",
    GET_ALL_CUSTOMERS: "get-all-customers",
    GET_ALL_MOVIES: "get-all-movies",
    GET_ALL_REQUESTS: "get-all-requests",
    REJECT_REQUEST: "reject-request",
    ACCEPT_REQUEST: "accept-request",
    REQUEST_MOVIE: "request-movie",
    SEARCH_MOVIE: "search-movie",
    RENT: "rent",
    RENEW: "renew",
    GET_ALL_RENTED_MOVIES: "list-all-rented-movies",
    GET_GENRES: "get-genres",
    WITHDRAW: "withdraw",
    ADD_FRIEND: "add-friend",
    DELETE_FRIEND: "delete-friend",
    GET_FRIENDS: "show-friends",
    GET_NON_FRIENDS: "show-non-friends",
    GET_RECEIVING_FRIEND_REQUESTS: "show-receiving-friend-requests",
    ANSWER_REQUESTS: "answer-request",
    GET_SENT_FRIEND_REQUESTS: "show-sent-friend-requests",
    RECOMMEND_MOVIE: "recommend-movie",
    GET_RECOMMENDED_MOVIES: "get-recommended-movies",
    ADD_FAVORITE: "add-favorite",
    DELETE_FAVORITE: "delete-favorite",
    GET_FAVORITE_LIST: "get-favorite-list",
    SEND_REVIEW: "write-review",
    SEND_RATE: "rate",
    GET_MOVIE_HISTORY: "show-movie-history",
    GET_REVIEWS_AND_RATINGS: "get-reviews-and-ratings",
    GET_FEE: "calculate-fee",
    GET_REPORT_ONE: "get-report-one",
    GET_REPORT_TWO: "get-report-two",
    GET_REPORT_THREE: "get-report-three",

};