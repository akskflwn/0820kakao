import { atom } from "recoil";

export const User = {
  id: null,
  password: null,
};

export const userState = atom({
  key: "userState",
  default: User,
});

export const loginState = atom({
  key: "loginState",
  default: false,
});
