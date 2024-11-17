package com.ked.tg.builders;

import com.ked.tg.dto.ButtonDto;
import com.ked.tg.utils.ButtonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageableInlineKeyboardMarkupBuilder extends InlineKeyboardMarkupBuilder {
    private static final Integer MAX_ROW_COUNT_IN_PAGE = 7;
    private int pageNumber;

    public static int getPageCount(List<ButtonDto> buttonDtoList) {
        int rowButtonCount = buttonDtoList.size();
        int pageCount = rowButtonCount / MAX_ROW_COUNT_IN_PAGE;
        if (rowButtonCount % MAX_ROW_COUNT_IN_PAGE > 0) {
            pageCount++;
        }

        return pageCount;
    }

    public static PageableInlineKeyboardMarkupBuilder create() {
        return new PageableInlineKeyboardMarkupBuilder();
    }

    @Override
    public InlineKeyboardMarkupBuilder setButtonList(List<ButtonDto> buttonDtoList) {
        for (int i = 0; i < buttonDtoList.size(); i++) {
            if (isCorrectRowForCurrentPage(pageNumber, i)) {
                addButton(buttonDtoList.get(i), i);
            }
        }

        if (buttonDtoList.size() > MAX_ROW_COUNT_IN_PAGE) {
            addMoveButtons();
        }

        return this;
    }

    private void addMoveButtons() {
        rowButtonList.add(ButtonUtil.pageMoveButtonList());
    }

    public PageableInlineKeyboardMarkupBuilder setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    private boolean isCorrectRowForCurrentPage(int pageNumber, int row) {
        int minRow = pageNumber * MAX_ROW_COUNT_IN_PAGE;
        int maxRow = minRow + MAX_ROW_COUNT_IN_PAGE - 1;
        return minRow <= row && row <= maxRow;
    }
}
