import axios from "axios";
import {SERVER_URL} from "./config"
export async function getProblemInfo(problemid) {
    try {
        const response = await axios.get(`${SERVER_URL}/problem/query/probleminfo/${problemid}`);
        return response.data;
    } catch (error) {
        return error;
    }
}