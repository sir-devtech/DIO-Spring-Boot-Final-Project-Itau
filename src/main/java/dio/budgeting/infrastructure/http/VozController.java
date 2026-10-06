package dio.budgeting.infrastructure.http;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/transactions")
@Profile("voz")
public class VozController {

    private final TranscriptionModel transcriptionModel;
    private final ChatClient assistente;
    private final TextToSpeechModel textToSpeechModel;

    public VozController(
            TranscriptionModel transcriptionModel,
            ChatClient assistente,
            TextToSpeechModel textToSpeechModel) {
        this.transcriptionModel = transcriptionModel;
        this.assistente = assistente;
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    public ResponseEntity<ByteArrayResource> ouvir(@RequestParam("file") MultipartFile file) {
        var texto = transcriptionModel.transcribe(file.getResource());
        var resposta = assistente.prompt().user(texto).call().content();
        byte[] audio = textToSpeechModel.call(resposta);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("resposta.mp3")
                        .build()
                        .toString())
                .body(new ByteArrayResource(audio));
    }
}
