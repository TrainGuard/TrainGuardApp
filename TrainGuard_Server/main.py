import asyncio
import aiohttp
from aiohttp import web
import json
import urllib.parse
from random import randrange


trains = {
    #"trains_count": 0
    #id : {
        #"name": 0,
        #"lat": 0,
        #"long": 0
    #}
}

global_train_pointer = 0

class JSONServer:
    def __init__(self):
        global trains
        self.app = web.Application()
        self.app.router.add_get("/add_train", self.handle_add)
        self.app.router.add_get("/update_train", self.handle_update)
        self.app.router.add_get("/get_trains", self.handle_get_trains)
        self.app.router.add_get("/get_trains_count", self.handle_get_trains_count)
        try:
            trains = self.load_json_data()
        except:
            pass

    def load_json_data(self):
        global trains
        global global_train_pointer
        try:
            with open("data.json", "r") as f:
                trains = json.load(f)
                global_train_pointer = trains["trains_count"]
                return json.load(f)
        except FileNotFoundError:
            return {"message": "ploho ochenb bce"}

    def save_json_data(self):
        global trains
        with open("data.json", "w") as f:
            json.dump(trains, f)

    async def handle_add(self, request):
        global trains
        try:
            trains = self.load_json_data()
        except:
            pass
        global global_train_pointer
        query_params = urllib.parse.parse_qs(request.query_string)
        name = query_params.get("name", [f"train{randrange(0, 100000)}"])[0]
        lat = query_params.get("lat", ["0"])[0]
        longt = query_params.get("long", ["0"])[0]
        trains[global_train_pointer] = {
            "name": name,
            "lat": float(lat),
            "long": float(longt)
        }
        global_train_pointer += 1
        trains["trains_count"] = global_train_pointer
        self.save_json_data()
        print(trains)

        data = {"id": global_train_pointer-1}
        return aiohttp.web.Response(text=json.dumps(data), content_type="application/json")

    async def handle_update(self, request):
        global trains
        try:
            trains = self.load_json_data()
        except:
            pass
        query_params = urllib.parse.parse_qs(request.query_string)
        idt = query_params.get("id", [f"{global_train_pointer}"])[0]
        lat = query_params.get("lat", ["0"])[0]
        longt = query_params.get("long", ["0"])[0]
        trains[idt]["lat"] = float(lat)
        trains[idt]["long"] = float(longt)
        self.save_json_data()
        print(trains)

        data = {"id": idt}
        return aiohttp.web.Response(text=json.dumps(data), content_type="application/json")

    async def handle_get_trains(self, request):
        global trains
        try:
            trains = self.load_json_data()
        except:
            pass

        query_params = urllib.parse.parse_qs(request.query_string)
        idt = query_params.get("id", [f"{global_train_pointer}"])[0]

        newout = trains[idt]

        print(newout)

        return aiohttp.web.Response(text=json.dumps(newout), content_type="application/json")

    async def handle_get_trains_count(self, request):
        global trains
        try:
            trains = self.load_json_data()
        except:
            pass
        return aiohttp.web.Response(text=str(trains["trains_count"]), content_type="application/json")

    def run(self):
        aiohttp.web.run_app(self.app, port=49187)

if __name__ == "__main__":
    server = JSONServer()
    print("Starting server...")
    server.run()