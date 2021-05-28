package be.zvz.kookie.network.mcpe.protocol

/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

class ModalFormRequestPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.MODAL_FORM_REQUEST_PACKET)

	var formId: Int
	var formData: string //json

	 static fun create(formId: Int, formData: string) : self{
		result = new self
		result.formId = formId
		result.formData = formData
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		formId = input.getUnsignedVarInt()
		formData = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(formId)
		output.putString(formData)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleModalFormRequest(this)
	}
}
