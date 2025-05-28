from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import random
from datetime import datetime

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/temperatures")
def get_temperatures():
    # 採用高斯分布生成溫度數據
    temperatures = [round(random.gauss(30, 5), 2) for _ in range(10)]
    timestamp = datetime.now().isoformat()
    return {"timestamp": timestamp, "temperatures": temperatures}
# Run the FastAPI server with: python -m uvicorn fastapi_backend:app --reload --port 8000