import spinal.core._
import spinal.lib.fsm._
import scala.language.postfixOps

import scala.io.Source


class top() extends Component {
  val io = new Bundle {
    val txp = out(Bool())
  }
  noIoPrefix()

  val tx_data = Reg(Bits(8 bits)) init 0
  val tx_en = Reg(Bool()) init False
  tx_en := False

  //下面代码等价于 reg [7:0] mem [4:0];initial $readmemh("output.hex",mem);
  val fileContents = Source.fromFile("output.hex").getLines.map(BigInt(_, 16)).toSeq
  val message_len = fileContents.length
  val rom = Mem(Bits(8 bits), message_len) initBigInt fileContents


  val u_uart_tx = new uart_tx()
  io.txp := u_uart_tx.io.txp
  u_uart_tx.io.tx_data := tx_data
  u_uart_tx.io.tx_en := tx_en
  val tx_busy = u_uart_tx.io.is_busy

  val counter = Reg(UInt(log2Up(27000000) bits)) init 0
  val send_index = Reg(UInt(log2Up(message_len) bits)) init 0

  object State extends SpinalEnum {
    val idle, work = newElement()
  }

  val state = RegInit(State.idle)
  switch(state) {
    is(State.idle) {
      counter := counter + 1
      when(counter === 27000000 - 1) {
        counter := 0
        state := State.work
      }
    }
    is(State.work) {
      when(!tx_busy) {
        tx_en := True
        tx_data := rom(send_index)
        send_index := send_index + 1
        when(send_index === message_len - 1) {
          send_index := 0
          state := State.idle
        }
        //   }
      }
    }
  }
}

object top extends App {
  SpinalConfig(defaultConfigForClockDomains = ClockDomainConfig(
    resetKind = ASYNC,
    resetActiveLevel = LOW //异步低电平有效复位
  ),
    _withEnumString = false, //不生成枚举状态机的string
    enumPrefixEnable = false //枚举名称不添加前缀
  ).generateVerilog(new top())
}