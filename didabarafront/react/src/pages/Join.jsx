import React from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import * as Yup from "yup";
import { useForm, Controller } from "react-hook-form";
import {
  Container,
  TextField,
  Button,
  Grid,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Typography,
} from "@mui/material";
import { Link, useNavigate } from "react-router-dom";

const Join = () => {
  const validationSchema = Yup.object().shape({
    username: Yup.string()
      .required("이메일을 입력해 주세요.")
      .email("올바른 형식으로"),
    password: Yup.string()
      .required("비밀번호를 입력해 주세요.")
      .min(6, "6글자 이상")
      .max(40, "40자 이하"),
    confirmPassword: Yup.string()
      .required("비밀번호를 확인해주세요")
      .oneOf([Yup.ref("password"), null], "비밀번호가 일치하지 않습니다."),
    nickname: Yup.string()
      .required("닉네임을 입력해 주세요.")
      .min(2, "닉넴은 두글자 이상")
      .max(15, "15글자 이하"),
  });

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({ resolver: yupResolver(validationSchema) });

  const navi = useNavigate();

  const onSubmit = (data) => {
    console.log(data.username);
    navi(`/email/config/${data.username}`);
  };

  return (
    <Container>
      <Grid>
        <Typography variant="h5">DIDABARA 회원 가입</Typography>
        <span>
          이미 가입하셨나요?
          <Link to="/login">
            <span>로그인 하기</span>
          </Link>
        </span>
        <form>
          <TextField
            required
            id="username"
            name="username"
            label="이메일"
            {...register("username")}
            error={errors.username ? true : false}
            helperText={errors.username?.message}
          />
          <TextField
            required
            id="password"
            name="password"
            label="비밀번호"
            type="password"
            {...register("password")}
            error={errors.password ? true : false}
            helperText={errors.password?.message}
          />
          <TextField
            required
            id="confirmPassword"
            name="confirmPassword"
            label="비밀번호 확인"
            type="password"
            {...register("confirmPassword")}
            error={errors.confirmPassword ? true : false}
            helperText={errors.confirmPassword?.message}
          />
          <TextField
            required
            id="nickname"
            name="nickname"
            label="닉네임"
            {...register("nickname")}
            error={errors.nickname ? true : false}
            helperText={errors.nickname?.message}
          />
          <FormControl>
            <InputLabel id="demo-simple-select-label">직업</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              // value={age}
              label="job"
              defaultValue=""
              // onChange={handleChange}
              {...register("job")}
            >
              <MenuItem value="무직">무직</MenuItem>
              <MenuItem value="학생">학생</MenuItem>
              <MenuItem value="컴퓨터">컴퓨터/인터넷</MenuItem>
              <MenuItem value="언론">언론</MenuItem>
              <MenuItem value="공무원">공무원</MenuItem>
              <MenuItem value="군인">군인</MenuItem>
              <MenuItem value="서비스업">서비스업</MenuItem>
              <MenuItem value="교육">교육</MenuItem>
              <MenuItem value="금융">금융/증권/보험업</MenuItem>
              <MenuItem value="유통업">유통업</MenuItem>
              <MenuItem value="예술">예술</MenuItem>
              <MenuItem value="의료">의료</MenuItem>
              <MenuItem value="법률">법률</MenuItem>
              <MenuItem value="건설업">건설업</MenuItem>
              <MenuItem value="제조업">제조업</MenuItem>
              <MenuItem value="부동산업">부동산업</MenuItem>
              <MenuItem value="운송업">운송업</MenuItem>
              <MenuItem value="농수산업">농/수/임/광산업</MenuItem>
              <MenuItem value="가사">가사</MenuItem>
              <MenuItem value="기타">기타</MenuItem>
            </Select>
          </FormControl>
          <Button variant="contained" onClick={handleSubmit(onSubmit)}>
            가입하기
          </Button>
        </form>
      </Grid>
    </Container>
  );
};

export default Join;
