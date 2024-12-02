package com.ked.interaction.steps.impl.user.statistic;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.entities.Statistic;
import com.ked.core.entities.User;
import com.ked.core.services.StatisticService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.CommandBotException;
import com.ked.tg.utils.DateUtil;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticLastDateInputStep extends InputStep {
    @Value("${report.file}")
    private String reportFile;

    @Value("${report.photo}")
    private String reportDiagram;

    private static final String PREPARE_MESSAGE_TEXT = "Введите конечную дату";

    private static final String EXCEPTION_MESSAGE_TEXT = "Неверный формат. Пример: 30.01.2023";

    private final StatisticService statisticService;

    private final UserService userService;

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        User user = userService.findByTgId(tgChat.getChatId());
        Statistic statistic = statisticService.setCreatedAt(user.getId());
        StatisticInfo info = statisticService.getTransactionStatisticByTimeInterval(
                user.getId(), statistic.getETimeInterval(), DateUtil.convertInstant(data)
        );

        sendDiagram(info.getDiagramPng(), tgChat, sender);
        sendReport(tgChat, sender, info, statistic);

        return 0;
    }

    private void sendDiagram(byte[] byteArray, TgChat tgChat, AbsSender sender) {
        try {
            createDiagramFile(byteArray);
            File reportDiagramFile = new File(reportDiagram);
            MessageUtil.sendPhoto(tgChat.getChatId(), reportDiagramFile, sender);
            reportDiagramFile.delete();
        } catch (IOException e) {
            throw new CommandBotException(e.getMessage(), "Что-то пошло не так. Обратитесь в поддержку");
        }
    }

    private void createDiagramFile(byte[] byteArray) throws IOException {
        Path filePath = Paths.get(reportDiagram);
        Files.write(filePath, byteArray);
    }

    private void sendReport(TgChat tgChat, AbsSender sender, StatisticInfo info, Statistic statistic) {
        File report = createDocument(info);
        MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setText("Ваш отчёт за ".concat(statistic.getETimeInterval().getValue()))
                        .setFile(report)
                        .sendDocument(tgChat.getChatId()),
                sender
        );
        report.delete();
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (!DateUtil.isValidDate(messageDto.getData())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    private File createDocument(StatisticInfo statisticInfo) {
        Document document = new Document();
        Section section = document.addSection();
        statisticInfo.getTransactions().forEach(transactionDto -> {
            section.addParagraph().setText(transactionDto.toString());
        });
        document.saveToFile(reportFile, FileFormat.PDF);

        return new File(reportFile);
    }
}
