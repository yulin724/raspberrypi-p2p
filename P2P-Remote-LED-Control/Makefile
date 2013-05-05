# Sample makefile

all: Device Client

cc=gcc
link=ld
cflags=-DLINUX
libs=-L. -lPPPP_API -L/usr/local/lib -lwiringPi
includes=include

Device: Device.cpp
	gcc $(cflags) $< -o $@ $(libs)

Client: Client.cpp
	gcc $(cflags) $< -o $@ $(libs)

start_device:
	LD_LIBRARY_PATH=. ./Device STAR-000004-DRJJH

clean:
	-rm Device Client
