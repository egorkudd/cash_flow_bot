package com.ked.tg.utils;

import com.ked.tg.dto.ButtonDto;
import com.ked.tg.enums.EChat;
import com.ked.tg.enums.EPageMove;
import com.ked.tg.enums.EYesNo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonUtil {
    public static final String OTHER_CHOICE = "Другое";
    public static final String OK_ANSWER = "OK";
    private static List<InlineKeyboardButton> pageMoveButtonList;
    private static List<ButtonDto> okList;
    private static List<ButtonDto> yesNoList;
    private static List<ButtonDto> chatTypeList;

    public static void addOtherChoice(List<ButtonDto> buttonDtoList) {
        buttonDtoList.add(new ButtonDto(OTHER_CHOICE, OTHER_CHOICE));
    }

    public static List<InlineKeyboardButton> pageMoveButtonList() {
        if (pageMoveButtonList == null) {
            pageMoveButtonList = getPageMoveButtonList();
        }


        return pageMoveButtonList;
    }

    public static List<ButtonDto> okButtonList() {
        if (okList == null) {
            okList = getOkButtonDtoList();
        }

        return okList;
    }

    public static List<ButtonDto> yesNoButtonList() {
        if (yesNoList == null) {
            yesNoList = getYesNoButtonDtoList();
        }

        return yesNoList;
    }

    public static List<ButtonDto> chatTypeButtonList() {
        if (chatTypeList == null) {
            chatTypeList = getChatTypeButtonDtoList();
        }

        return chatTypeList;
    }

    private static List<InlineKeyboardButton> getPageMoveButtonList() {
        return Arrays.asList(
                (new ButtonDto(EPageMove.PREV.name(), EPageMove.PREV.getValue())).toKeyboardButton(),
                (new ButtonDto(EPageMove.NEXT.name(), EPageMove.NEXT.getValue())).toKeyboardButton()
        );
    }

    private static List<ButtonDto> getOkButtonDtoList() {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        buttonDtoList.add(new ButtonDto(OK_ANSWER, OK_ANSWER));
        return buttonDtoList;
    }

    private static List<ButtonDto> getYesNoButtonDtoList() {
        EYesNo[] eYesNoArray = EYesNo.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EYesNo eYesNo : eYesNoArray) {
            buttonDtoList.add(new ButtonDto(eYesNo.toString(), eYesNo.getValue()));
        }

        return buttonDtoList;
    }

    private static List<ButtonDto> getChatTypeButtonDtoList() {
        EChat[] eChats = EChat.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EChat eChat : eChats) {
            buttonDtoList.add(new ButtonDto(eChat.toString(), eChat.getValue()));
        }

        return buttonDtoList;
    }
}
