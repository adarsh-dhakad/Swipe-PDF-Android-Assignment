package com.swipe.pdf;


import android.graphics.Bitmap;
import android.view.View;

public class GkemonGenerator {


//    public void generate(){
//        PdfGenerator.getBuilder()
//                .setContext(context)
//                .fromLayoutXMLSource()
//                .fromLayoutXML(R.layout.layout_print,R.layout.layout_print)
//                /* "fromLayoutXML()" takes array of layout resources.
//                 * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
//                .setFileName("Test-PDF")
//                /* It is file name */
//                .setFolderName("FolderA/FolderB/FolderC")
//                /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
//                 * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
//                 * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
//                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
//                /* It true then the generated pdf will be shown after generated. */
//                .build(new PdfGeneratorListener() {
//                    @Override
//                    public void onFailure(FailureResponse failureResponse) {
//                        super.onFailure(failureResponse);
//                        /* If pdf is not generated by an error then you will findout the reason behind it
//                         * from this FailureResponse. */
//                    }
//                    @Override
//                    public void onStartPDFGeneration() {
//                        /*When PDF generation begins to start*/
//                    }
//
//                    @Override
//                    public void onFinishPDFGeneration() {
//                        /*When PDF generation is finished*/
//                    }
//
//                    @Override
//                    public void showLog(String log) {
//                        super.showLog(log);
//                        /*It shows logs of events inside the pdf generation process*/
//                    }
//
//                    @Override
//                    public void onSuccess(SuccessResponse response) {
//                        super.onSuccess(response);
//                        /* If PDF is generated successfully then you will find SuccessResponse
//                         * which holds the PdfDocument,File and path (where generated pdf is stored)*/
//
//                    }
//                });
//    }
}
