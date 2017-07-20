package cc.metapro.nfc.util

import android.app.Activity
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.NfcEvent

fun NfcAdapter.setupBeamPushUriCallback(activity: Activity) {
    class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {

        private val mFileUris = arrayListOf<Uri>()

        override fun createBeamUris(p0: NfcEvent?): Array<Uri> {
//            String transferFile = "transferimage.jpg";
//            File extDir = getExternalFilesDir(null);
//            File requestFile = new File(extDir, transferFile);
//            requestFile.setReadable(true, false);
//             Get a URI for the File and add it to the list of URIs
//            fileUri = Uri.fromFile(requestFile);
//            if (fileUri != null) {
//                mFileUris[0] = fileUri;
//            } else {
//                Log.e("My Activity", "No File URI available for file.");
//            }

            return mFileUris.toArray(arrayOf<Uri>())
        }

    }
    this.setBeamPushUrisCallback(FileUriCallback(), activity)
}