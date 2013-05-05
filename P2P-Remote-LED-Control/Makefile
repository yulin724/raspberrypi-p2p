# Sample makefile

all: Device

cc=gcc
link=ld
cflags=-DLINUX
libs=-L. -lPPPP_API -L/usr/local/lib -lwiringPi
includes=include

Device: Device.cpp
	gcc $(cflags) $< -o $@ $(libs)

clean:
	-rm Device
