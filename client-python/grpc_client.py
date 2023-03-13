import grpc
import datetime
from google.protobuf.timestamp_pb2 import Timestamp

import pingpong_pb2_grpc
import pingpong_pb2


def run():
    with grpc.insecure_channel('localhost:8080') as channel:
        stub = pingpong_pb2_grpc.PingPongServiceStub(channel)

        t = datetime.datetime.now().timestamp()
        seconds = int(t)
        nanos = int(t % 1 * 1e9)

        proto_timestamp = Timestamp(seconds=seconds, nanos=nanos)

        print("Sending request for one response");

        response = stub.getResponse(pingpong_pb2.PingRequest(message='Ping', request_date=proto_timestamp))

        print("Response from server: " + response.message)

        print("Sending request for stream responses")

        responses = stub.getMultipleResponses(pingpong_pb2.PingRequest(message='Ping', request_date=proto_timestamp))
        for r in responses:
            print("Response from server: " + r.message)

run()
