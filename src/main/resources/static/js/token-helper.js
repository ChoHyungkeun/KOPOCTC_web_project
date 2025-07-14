async function requestWithRetry(url, options = {}) {
    options.credentials = "include"; // 쿠키 포함
    options.headers = {
        ...options.headers,
        "Accept": "application/json",
    };

    console.log("요청 시작:", url, options);
    const res = await fetch(url, options);
    console.log("응답 받음:", res.status);

    if (res.status === 401) {
        console.log("AccessToken 만료. 재발급 시도...");
        const refreshRes = await fetch("/reissue", {
            method: "POST",
            credentials: "include",
            headers: {
                "Accept": "application/json",
            },
        });

        if (refreshRes.ok) {
            console.log("재발급 성공. 원 요청 재시도...");
            return await fetch(url, options);  // 원래 요청 재시도
        } else {
            console.log("토큰 재발급 실패. 로그인 페이지로 이동");
            window.location.href = "/login";
            return;
        }
    }

    return res;
}