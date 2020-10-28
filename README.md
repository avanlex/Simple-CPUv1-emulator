# Simple-CPUv1-emulator
Simple CPU v1 Java emulation. 

Ispired by http://www.simplecpudesign.com/simple_cpu_v1/

## Architecture
<img src="http://www.simplecpudesign.com/simple_cpu_v1/Images/arch.jpg" alt="Architecture" width="600">

<img src="http://www.simplecpudesign.com/simple_cpu_v1/Images/system.jpg" alt="Architecture" width="600">

### RAM
<img src="http://www.simplecpudesign.com/simple_cpu_v1/Images/ram.jpg" alt="Ram" width="600">


### ALU

|S4|S3|S2|S1|S0|Z                 |
|--|--|--|--|--|-----------------|
|0 |0 |0 |0 |0 |ADD (A+B)        |
|0 |0 |0 |0 |1 |BITWISE AND (A&B)|
|0 |0 |0 |1 |0 |INPUT A          |
|0 |0 |0 |1 |1 |INPUT B          |
|0 |1 |1 |0 |0 |SUBTRACT (A-B)   |
|1 |0 |1 |0 |0 |INCREMENT (A+1)  |
|1 |0 |0 |0 |0 |INPUT A          |
|0 |0 |1 |0 |0 |ADD (A+B)+1      |
|0 |1 |0 |0 |0 |SUBTRACT (A-B)-1 |